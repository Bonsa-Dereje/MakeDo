<script>
  import { onMount, onDestroy } from 'svelte';

  // ── Data ─────────────────────────────────────────────────────────────────
  const SNAP = {
    host: 'studio-pro',
    os: 'macOS 15.4 Sequoia',
    cpu: { brand: 'Apple M3 Pro', cores_p: 12, cores_l: 12, freq: 3700, arch: 'ARM64', temp: 47.2 },
    ram: { total: 36, used: 14.7, pct: 40.8 },
    disk: { name: 'Apple SSD AP2048Z', kind: 'NVMe SSD', total: 2000, used: 843, pct: 42.2, health: 'Good', score: 96 },
    gpu: { name: 'Apple M3 Pro GPU', vram: 18, kind: 'Integrated (Metal)' },
    screen: { res: '3456×2234', size: '14.2"', hz: 120 },
    score: { cpu: 7840, ram: 9000, disk: 9200, gpu: 5000, overall: 7760, rating: 'Good' },
  };

  const UPGRADES = [
    { level: 'good', text: 'RAM: 36 GB unified — excellent for pro workloads' },
    { level: 'good', text: 'Storage: NVMe SSD 2 TB — fast and spacious' },
    { level: 'warn', text: 'GPU: integrated only — no dedicated VRAM for 3D/ML' },
    { level: 'good', text: 'Display: 120 Hz ProMotion — sharp and fluid' },
    { level: 'warn', text: 'Ports: limited I/O — may need hub for peripherals' },
  ];

  // ── State ─────────────────────────────────────────────────────────────────
  let running = false;
  let elapsed = 0;
  let cpuSamples = [];
  let logLines = [];
  let stressResult = null;
  let timer = null;
  let benchDuration = 40;

  // Donut animated values (displayed inside each ring)
  let donutVals = { cpu: '—', ram: '—', disk: '—', gpu: '—' };
  // Donut stroke-dashoffset values
  let donutOffsets = { cpu: 0, ram: 0, disk: 0, gpu: 0 };
  const DONUT_R = 29;
  const DONUT_CIRC = 2 * Math.PI * DONUT_R;

  // Score arc
  let scoreArcD = '';
  let scoreNum = '—';
  let scoreColor = '#FF6B00';

  // Result tiles
  let rCpu = '—', rRam = '—', rDisk = '—', rTemp = '—';
  let rTempColor = 'inherit';
  let resultsFadeIn = false;

  // Log head state
  let logState = 'idle'; // 'idle' | 'running' | 'complete' | 'stopped'

  const f1 = n => n.toFixed(1);

  // ── Helpers ───────────────────────────────────────────────────────────────
  function pctToOffset(pct) {
    return DONUT_CIRC * (1 - pct / 100);
  }

  let donutCurrent = { cpu: 0, ram: 0, disk: 0, gpu: 0 };

  function animateDonut(id, targetPct, valText) {
    const from = donutCurrent[id] || 0;
    const dur = 1000;
    let start = null;
    function step(ts) {
      if (!start) start = ts;
      const p = Math.min((ts - start) / dur, 1);
      const ease = 1 - Math.pow(1 - p, 3);
      const cur = from + (targetPct - from) * ease;
      donutOffsets[id] = pctToOffset(cur);
      donutOffsets = donutOffsets; // trigger reactivity
      donutVals[id] = valText || (Math.round(cur) + '%');
      donutVals = donutVals;
      if (p < 1) requestAnimationFrame(step);
      else donutCurrent[id] = targetPct;
    }
    requestAnimationFrame(step);
  }

  function addLog(msg, cls = '') {
    logLines = [...logLines.slice(-8), { t: elapsed, msg, cls }];
  }

  // ── Score arc animation ───────────────────────────────────────────────────
  function buildArc(score) {
    const r = 36, cx = 44, cy = 44;
    const sA = -225 * (Math.PI / 180);
    const sw = 270 * (Math.PI / 180);
    const ap = a => `${cx + r * Math.cos(a)},${cy + r * Math.sin(a)}`;
    const arc = (s, e) => {
      const l = e - s > Math.PI ? 1 : 0;
      return `M ${ap(s)} A ${r} ${r} 0 ${l} 1 ${ap(e)}`;
    };
    const frac = Math.max(score / 10000, 0.001);
    return arc(sA, sA + sw * frac);
  }

  function animateScoreArc(score) {
    scoreColor = score >= 8000 ? '#16A34A' : score >= 6000 ? '#FF6B00' : score >= 4000 ? '#D97706' : '#EF4444';
    const dur = 1400;
    let start = null;
    function step(ts) {
      if (!start) start = ts;
      const p = Math.min((ts - start) / dur, 1);
      const ease = 1 - Math.pow(1 - p, 3);
      const cur = score * ease;
      scoreArcD = buildArc(cur);
      scoreNum = Math.round(cur).toString();
      if (p < 1) requestAnimationFrame(step);
    }
    requestAnimationFrame(step);
  }

  // ── Spark SVG ─────────────────────────────────────────────────────────────
  function sparkSVG(data, w = 224, h = 36, col = '#FF6B00') {
    if (data.length < 2) return null;
    const max = Math.max(...data, 100);
    const pts = data.map((v, i) => {
      const x = (i / (data.length - 1)) * w;
      const y = h - (v / max) * (h - 4) - 2;
      return `${x},${y}`;
    }).join(' ');
    const area = `${pts} ${w},${h} 0,${h}`;
    return { pts, area, col, w, h };
  }

  $: spark = sparkSVG(cpuSamples.slice(-40), 224, 36);

  // ── Verdict ───────────────────────────────────────────────────────────────
  $: verdict = SNAP.score.overall >= 8000
    ? { icon: '★', bg: '#F0FDF4', txt: 'Ready to sell at a premium. High-performing across all specs.', c: '#166534' }
    : SNAP.score.overall >= 6000
    ? { icon: '◆', bg: '#FFF7ED', txt: 'Solid daily driver. Worth upgrading GPU for creative work.', c: '#92400E' }
    : { icon: '▲', bg: '#FEF2F2', txt: 'Ageing. Consider RAM or storage upgrade before selling.', c: '#991B1B' };

  // ── Stress test ───────────────────────────────────────────────────────────
  function startStress() {
    if (running) return;
    running = true;
    elapsed = 0;
    cpuSamples = [];
    logLines = [];
    stressResult = null;
    rCpu = '—'; rRam = '—'; rDisk = '—'; rTemp = '—';
    resultsFadeIn = false;
    logState = 'running';

    addLog('initialising benchmark suite…', 'hi');

    const stepsPerSec = 2;
    const total = benchDuration * stepsPerSec;
    let step = 0;
    let peakTemp = 47;
    let peakUsage = 0;

    timer = setInterval(() => {
      if (!running) { clearInterval(timer); return; }
      step++;
      elapsed = Math.round(step / stepsPerSec);
      const pct = step / total;

      const cpuU = 70 + Math.random() * 28;
      const temp = 55 + pct * 25 + Math.random() * 8;
      peakTemp = Math.max(peakTemp, temp);
      peakUsage = Math.max(peakUsage, cpuU);
      cpuSamples = [...cpuSamples, cpuU];

      animateDonut('cpu', Math.min(cpuU, 99), Math.round(cpuU) + '%');
      animateDonut('ram', 40 + Math.random() * 10, '14.7 GB');

      if (step === 4) addLog('cpu stress: all cores pinned', 'hi');
      if (step === 8) { addLog('thermal: ' + f1(temp) + '°C — nominal', 'good'); animateDonut('disk', 65 + Math.random() * 20, '4.1 GB/s'); }
      if (step === 14) addLog('memory bandwidth saturated', '');
      if (step === 20) { addLog('gpu compute load applied', ''); animateDonut('gpu', 55 + Math.random() * 30, Math.round(55 + Math.random() * 30) + '%'); }
      if (temp > 72 && step % 4 === 0) addLog('thermal: ' + f1(temp) + '°C', 'warn');
      if (step % 6 === 0) addLog('cpu: ' + f1(cpuU) + '%  temp: ' + f1(temp) + '°C', '');

      if (step >= total) {
        clearInterval(timer);
        finishStress(peakTemp, peakUsage);
      }
    }, 1000 / stepsPerSec);
  }

  function finishStress(peakTemp, peakUsage) {
    running = false;
    const throttled = peakTemp > 85;
    const { cpu: cpuScore, ram: ramScore, disk: diskScore, gpu: gpuScore } = SNAP.score;

    stressResult = { cpuScore, ramScore, diskScore, gpuScore, peakTemp, peakUsage, throttled };
    logState = 'complete';

    addLog('benchmark complete', 'good');
    if (throttled) addLog('thermal throttling detected — check cooling', 'alert');
    else addLog('no throttling — cooling is healthy', 'good');

    animateDonut('cpu', (cpuScore / 10000) * 100, cpuScore.toString());
    animateDonut('ram', (ramScore / 10000) * 100, ramScore.toString());
    animateDonut('disk', (diskScore / 10000) * 100, diskScore.toString());
    animateDonut('gpu', (gpuScore / 10000) * 100, gpuScore.toString());

    const col = peakTemp > 85 ? 'var(--red)' : peakTemp > 70 ? 'var(--amber)' : 'var(--green)';

    setTimeout(() => {
      rCpu = cpuScore;
      rRam = ramScore;
      rDisk = diskScore;
      rTemp = f1(peakTemp) + '°C';
      rTempColor = col;
      resultsFadeIn = true;
    }, 800);
  }

  function stopStress() {
    running = false;
    clearInterval(timer);
    addLog('test stopped by user', 'warn');
    logState = 'stopped';
  }

  function resetAndRun() {
    stressResult = null;
    startStress();
  }

  // ── Lifecycle ─────────────────────────────────────────────────────────────
  onMount(() => {
    setTimeout(() => animateScoreArc(SNAP.score.overall), 400);
    // initialise donut offsets to full (empty)
    donutOffsets = { cpu: DONUT_CIRC, ram: DONUT_CIRC, disk: DONUT_CIRC, gpu: DONUT_CIRC };
  });

  onDestroy(() => { if (timer) clearInterval(timer); });

  const DONUT_COLORS = { cpu: '#FF6B00', ram: '#7C3AED', disk: '#16A34A', gpu: '#2563EB' };
  const DONUT_KEYS = ['cpu', 'ram', 'disk', 'gpu'];

  const dotColor = level => level === 'good' ? '#16A34A' : level === 'warn' ? '#D97706' : '#EF4444';
</script>

<!-- Fonts -->
<svelte:head>
  <link href="https://fonts.googleapis.com/css2?family=Space+Grotesk:wght@400;500;600;700&family=JetBrains+Mono:wght@400;600;700&display=swap" rel="stylesheet" />
</svelte:head>

<div class="app">
  <!-- Top bar -->
  <div class="topbar">
    <div class="logo">BENCH<em>Y</em></div>
    <div class="topbar-right">
      <span class="hostname-chip">{SNAP.host}</span>
      {#if running}
        <button class="chip danger" on:click={stopStress}>stop</button>
      {:else}
        <button class="chip active" on:click={stressResult ? resetAndRun : startStress}>
          {stressResult ? 'run again' : 'run benchmark'}
        </button>
      {/if}
    </div>
  </div>

  <!-- Body -->
  <div class="body">
    <!-- LEFT: Specs -->
    <div class="panel left">
      <div class="section-eyebrow">System specs</div>

      <div class="spec-card">
        <div class="spec-grid">
          <div class="spec-item">
            <div class="spec-k">Processor</div>
            <div class="spec-v" style="font-size:10px">{SNAP.cpu.brand}</div>
          </div>
          <div class="spec-item">
            <div class="spec-k">Architecture</div>
            <div class="spec-v">{SNAP.cpu.arch} · {SNAP.cpu.cores_p}C</div>
          </div>
          <div class="spec-item">
            <div class="spec-k">Memory</div>
            <div class="spec-v accent">{SNAP.ram.total} GB unified</div>
          </div>
          <div class="spec-item">
            <div class="spec-k">Storage</div>
            <div class="spec-v good">{SNAP.disk.kind} {SNAP.disk.total} GB</div>
          </div>
          <div class="spec-item">
            <div class="spec-k">GPU</div>
            <div class="spec-v" style="font-size:10px">{SNAP.gpu.name}</div>
          </div>
          <div class="spec-item">
            <div class="spec-k">VRAM</div>
            <div class="spec-v warn">{SNAP.gpu.vram} GB shared</div>
          </div>
          <div class="spec-item">
            <div class="spec-k">Display</div>
            <div class="spec-v">{SNAP.screen.size} · {SNAP.screen.hz} Hz</div>
          </div>
          <div class="spec-item">
            <div class="spec-k">Resolution</div>
            <div class="spec-v" style="font-size:10px">{SNAP.screen.res}</div>
          </div>
          <div class="spec-item" style="grid-column:span 2;border-right:none">
            <div class="spec-k">OS</div>
            <div class="spec-v" style="font-size:10px;font-weight:600">{SNAP.os}</div>
          </div>
        </div>
      </div>

      <!-- Verdict bar -->
      <div class="verdict-bar" style="background:{verdict.bg};border-color:{verdict.c}33">
        <div class="verdict-icon" style="background:{verdict.c}18;color:{verdict.c};font-family:var(--mono);font-weight:700">{verdict.icon}</div>
        <div class="verdict-text">
          <h3 style="color:{verdict.c}">{SNAP.score.rating} machine</h3>
          <p>{verdict.txt}</p>
        </div>
        <!-- Score arc -->
        <div style="margin-left:auto;flex-shrink:0">
          <svg width="80" height="80" viewBox="0 0 88 88">
            <!-- Track -->
            <path
              d="M {44 + 36*Math.cos(-225*(Math.PI/180))},{44 + 36*Math.sin(-225*(Math.PI/180))} A 36 36 0 1 1 {44 + 36*Math.cos(45*(Math.PI/180))},{44 + 36*Math.sin(45*(Math.PI/180))}"
              fill="none" stroke="#f0f0f0" stroke-width="6" stroke-linecap="round"
            />
            <!-- Fill -->
            {#if scoreArcD}
              <path d={scoreArcD} fill="none" stroke={scoreColor} stroke-width="6" stroke-linecap="round"/>
            {/if}
            <text x="44" y="46" text-anchor="middle" fill={scoreColor} font-family="JetBrains Mono" font-weight="700" font-size="16">{scoreNum}</text>
            <text x="44" y="57" text-anchor="middle" fill="#ccc" font-family="JetBrains Mono" font-size="7">/10000</text>
          </svg>
        </div>
      </div>

      <!-- Upgrades -->
      <div class="section-eyebrow" style="margin-top:2px">Buy / sell notes</div>
      <div class="upgrade-list">
        {#each UPGRADES as u}
          <div class="upgrade-item {u.level}">
            <div class="upgrade-dot" style="background:{dotColor(u.level)}"></div>
            {u.text}
          </div>
        {/each}
      </div>
    </div>

    <!-- RIGHT: Benchmark -->
    <div class="panel">
      <div class="section-eyebrow">Benchmark</div>

      <!-- Donuts row -->
      <div class="donuts-row">
        {#each DONUT_KEYS as key}
          <div class="donut-wrap">
            <svg width="72" height="72" viewBox="0 0 72 72" style="transform:rotate(-90deg)">
              <circle cx="36" cy="36" r={DONUT_R} fill="none" stroke="#f0f0f0" stroke-width="5.5"/>
              <circle
                cx="36" cy="36" r={DONUT_R}
                fill="none"
                stroke={DONUT_COLORS[key]}
                stroke-width="5.5"
                stroke-dasharray={DONUT_CIRC}
                stroke-dashoffset={donutOffsets[key] ?? DONUT_CIRC}
                stroke-linecap="round"
                style="transition:stroke-dashoffset 1.2s cubic-bezier(.4,0,.2,1)"
              />
            </svg>
            <div class="donut-val" style="color:{DONUT_COLORS[key]};position:absolute;top:50%;left:50%;transform:translate(-50%,-55%);font-size:11px">
              {donutVals[key]}
            </div>
            <div class="donut-lbl">{key.toUpperCase()}</div>
          </div>
        {/each}
      </div>

      <!-- Stress log -->
      <div class="stress-log">
        <div class="log-head">
          {#if logState === 'running'}
            <div class="log-pulse"></div><span>Live log</span>
          {:else if logState === 'complete'}
            <span>Complete</span>
          {:else if logState === 'stopped'}
            <span>Stopped</span>
          {:else}
            <span>Run log</span>
          {/if}
        </div>
        <div class="log-lines">
          {#if logLines.length === 0}
            <div class="log-line" style="color:var(--faint)">Press "run benchmark" to start a full system test.</div>
          {:else}
            {#each logLines as line}
              <div class="log-line {line.cls}">
                <span class="log-time">{line.t}s</span>{line.msg}
              </div>
            {/each}
          {/if}
        </div>
      </div>

      <!-- Spark chart -->
      <div class="mini-chart">
        {#if spark}
          <svg width="224" height="36" viewBox="0 0 224 36" style="display:block">
            <polygon points="{spark.area}" fill="{spark.col}18"/>
            <polyline points="{spark.pts}" fill="none" stroke="{spark.col}" stroke-width="1.5" stroke-linejoin="round" stroke-linecap="round"/>
          </svg>
        {:else}
          <svg width="224" height="36">
            <text x="112" y="20" fill="#ddd" font-family="JetBrains Mono" font-size="9" text-anchor="middle">run a benchmark first</text>
          </svg>
        {/if}
      </div>

      <!-- Result tiles -->
      <div class="results-grid" class:fade-in={resultsFadeIn}>
        <div class="result-tile">
          <div class="result-lbl">CPU score</div>
          <div class="result-val">{rCpu}</div>
          <div class="result-sub">multi-core</div>
        </div>
        <div class="result-tile">
          <div class="result-lbl">RAM score</div>
          <div class="result-val">{rRam}</div>
          <div class="result-sub">{SNAP.ram.total} GB unified</div>
        </div>
        <div class="result-tile">
          <div class="result-lbl">Disk score</div>
          <div class="result-val">{rDisk}</div>
          <div class="result-sub">seq read/write</div>
        </div>
        <div class="result-tile">
          <div class="result-lbl">Peak temp</div>
          <div class="result-val" style="color:{rTempColor}">{rTemp}</div>
          <div class="result-sub">during stress</div>
        </div>
      </div>

      <!-- CTA -->
      <div class="stress-cta">
        <button
          class="run-btn"
          class:running
          disabled={running}
          on:click={stressResult ? resetAndRun : startStress}
        >
          {#if running}running…{:else if stressResult}⟳ run again{:else}⚡ run full benchmark{/if}
        </button>
        <select class="dur-sel" bind:value={benchDuration} disabled={running}>
          <option value={20}>20s</option>
          <option value={40}>40s</option>
          <option value={60}>60s</option>
        </select>
      </div>
    </div>
  </div>
</div>

<style>
  *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }

  :global(:root) {
    --bg: #f7f7f5;
    --card: #ffffff;
    --border: #e9e9e6;
    --border2: #f0f0ee;
    --text: #111;
    --muted: #999;
    --faint: #c8c8c4;
    --orange: #FF6B00;
    --green: #16A34A;
    --red: #EF4444;
    --amber: #D97706;
    --blue: #2563EB;
    --purple: #7C3AED;
    --mono: 'JetBrains Mono', monospace;
    --sans: 'Space Grotesk', sans-serif;
  }

  .app {
    font-family: var(--sans);
    background: var(--bg);
    background-image: radial-gradient(circle, #d8d8d4 1px, transparent 1px);
    background-size: 22px 22px;
    min-height: 640px;
  }

  /* Top bar */
  .topbar {
    display: flex; align-items: center; justify-content: space-between;
    padding: 11px 20px; background: rgba(247,247,245,.95);
    border-bottom: 1px solid var(--border); backdrop-filter: blur(10px);
  }
  .logo { font-family: var(--mono); font-size: 12px; font-weight: 700; letter-spacing: .2em; color: var(--text); }
  .logo em { color: var(--orange); font-style: normal; }
  .topbar-right { display: flex; gap: 6px; align-items: center; }
  .hostname-chip {
    font-family: var(--mono); font-size: 9px; color: var(--muted);
    padding: 4px 10px; border: 1px solid var(--border); border-radius: 5px;
  }

  /* Chips */
  .chip {
    padding: 4px 12px; border-radius: 5px; border: 1px solid var(--border);
    font-family: var(--mono); font-size: 9px; font-weight: 700; letter-spacing: .1em; text-transform: uppercase;
    cursor: pointer; background: var(--card); color: var(--muted); transition: all .15s;
  }
  .chip:hover { border-color: #bbb; color: var(--text); }
  .chip.active { background: var(--text); color: #fff; border-color: var(--text); }
  .chip.danger { background: var(--red); color: #fff; border-color: var(--red); }

  /* Layout */
  .body { display: grid; grid-template-columns: 1fr 1fr; gap: 0; height: calc(100% - 44px); }
  .panel { padding: 16px 18px; display: flex; flex-direction: column; gap: 12px; }
  .panel.left { border-right: 1px solid var(--border); }

  .section-eyebrow {
    font-family: var(--mono); font-size: 8px; font-weight: 700;
    letter-spacing: .22em; text-transform: uppercase; color: var(--faint); margin-bottom: 6px;
  }

  /* Spec card */
  .spec-card { background: var(--card); border: 1px solid var(--border); border-radius: 10px; padding: 14px 16px; }
  .spec-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 0; }
  .spec-item { padding: 9px 10px; border-bottom: 1px solid var(--border2); }
  .spec-item:nth-child(odd) { border-right: 1px solid var(--border2); }
  .spec-item:nth-last-child(-n+2) { border-bottom: none; }
  .spec-k { font-family: var(--mono); font-size: 7px; letter-spacing: .18em; text-transform: uppercase; color: var(--faint); margin-bottom: 2px; }
  .spec-v { font-family: var(--mono); font-size: 12px; font-weight: 700; color: var(--text); line-height: 1.2; }
  .spec-v.accent { color: var(--orange); }
  .spec-v.good { color: var(--green); }
  .spec-v.warn { color: var(--amber); }
  .spec-v.bad { color: var(--red); }

  /* Verdict */
  .verdict-bar {
    display: flex; align-items: center; gap: 10px;
    background: var(--card); border: 1px solid var(--border);
    border-radius: 10px; padding: 12px 16px;
  }
  .verdict-icon {
    width: 36px; height: 36px; border-radius: 8px;
    display: flex; align-items: center; justify-content: center; font-size: 16px; flex-shrink: 0;
  }
  .verdict-text h3 { font-size: 13px; font-weight: 700; color: var(--text); margin-bottom: 2px; }
  .verdict-text p { font-family: var(--mono); font-size: 9px; color: var(--muted); line-height: 1.5; }

  /* Upgrades */
  .upgrade-list { display: flex; flex-direction: column; gap: 4px; }
  .upgrade-item {
    display: flex; align-items: flex-start; gap: 7px;
    padding: 7px 10px; border-radius: 7px; font-family: var(--mono); font-size: 9px; line-height: 1.4;
  }
  .upgrade-item.good { background: #F0FDF4; color: #166534; border: 1px solid #BBF7D0; }
  .upgrade-item.warn { background: #FFFBEB; color: #92400E; border: 1px solid #FDE68A; }
  .upgrade-item.bad  { background: #FEF2F2; color: #991B1B; border: 1px solid #FECACA; }
  .upgrade-dot { width: 6px; height: 6px; border-radius: 50%; flex-shrink: 0; margin-top: 2px; }

  /* Donuts */
  .donuts-row {
    display: flex; justify-content: space-around; align-items: center;
    background: var(--card); border: 1px solid var(--border); border-radius: 10px; padding: 14px 10px;
  }
  .donut-wrap { display: flex; flex-direction: column; align-items: center; gap: 4px; position: relative; }
  .donut-lbl { font-family: var(--mono); font-size: 7px; letter-spacing: .16em; text-transform: uppercase; color: var(--faint); }
  .donut-val { font-family: var(--mono); font-size: 11px; font-weight: 700; }

  /* Log */
  .stress-log {
    background: var(--card); border: 1px solid var(--border); border-radius: 10px; padding: 12px 14px; flex: 1;
  }
  .log-head {
    font-family: var(--mono); font-size: 8px; font-weight: 700;
    letter-spacing: .2em; text-transform: uppercase; color: var(--faint);
    margin-bottom: 8px; display: flex; align-items: center; gap: 6px;
  }
  .log-pulse { width: 5px; height: 5px; border-radius: 50%; background: var(--orange); animation: lp 1.2s ease-in-out infinite; }
  @keyframes lp { 0%,100%{opacity:1;transform:scale(1)} 50%{opacity:.3;transform:scale(.7)} }
  .log-lines { display: flex; flex-direction: column; gap: 3px; }
  .log-line { font-family: var(--mono); font-size: 9px; color: var(--muted); display: flex; gap: 8px; align-items: baseline; }
  .log-time { color: var(--faint); flex-shrink: 0; width: 36px; }
  .log-line.hi { color: var(--text); }
  .log-line.alert { color: var(--red); }
  .log-line.good { color: var(--green); }
  .log-line.warn { color: var(--amber); }

  /* Spark */
  .mini-chart {
    height: 40px; background: var(--card); border: 1px solid var(--border);
    border-radius: 10px; padding: 8px 12px; overflow: hidden;
  }

  /* Results */
  .results-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 6px; }
  .result-tile { background: var(--card); border: 1px solid var(--border); border-radius: 8px; padding: 10px 12px; }
  .result-lbl { font-family: var(--mono); font-size: 7px; letter-spacing: .16em; text-transform: uppercase; color: var(--faint); margin-bottom: 3px; }
  .result-val { font-family: var(--mono); font-size: 18px; font-weight: 700; color: var(--text); }
  .result-sub { font-family: var(--mono); font-size: 8px; color: var(--muted); margin-top: 1px; }

  /* CTA */
  .stress-cta { display: flex; gap: 6px; align-items: center; }
  .run-btn {
    padding: 9px 18px; border-radius: 7px; border: none;
    font-family: var(--mono); font-size: 10px; font-weight: 700; letter-spacing: .1em; text-transform: uppercase;
    cursor: pointer; background: var(--text); color: #fff; transition: all .2s; flex: 1;
  }
  .run-btn:hover:not(:disabled) { background: #333; transform: translateY(-1px); }
  .run-btn:disabled { background: #ddd; color: #bbb; cursor: not-allowed; transform: none; }
  .run-btn.running { background: var(--orange); animation: pulse-btn 1.5s ease-in-out infinite; }
  @keyframes pulse-btn { 0%,100%{opacity:1} 50%{opacity:.8} }
  .dur-sel {
    padding: 8px 10px; border-radius: 7px; border: 1px solid var(--border);
    font-family: var(--mono); font-size: 9px; background: var(--card); color: var(--muted); cursor: pointer;
  }

  /* Animations */
  @keyframes fadeIn { from{opacity:0;transform:translateY(4px)} to{opacity:1;transform:translateY(0)} }
  .fade-in { animation: fadeIn .3s ease forwards; }
</style>