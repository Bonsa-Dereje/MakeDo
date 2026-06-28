# MakeDo Laptop Finder — Design Specification

## Overview

MakeDo helps users discover the right laptop based on their profession and the applications they use. The experience should feel:

* Fast
* Technical
* Minimal
* Trustworthy
* E-commerce inspired

---

# Design Language

## Personality

* Industrial
* Modern
* Technical
* Lightweight
* Performance-focused

Think:

* Apple Store simplicity
* Stripe typography
* Linear's cleanliness
* Slight gaming hardware aesthetic

---

# Color System

## Primary Colors

| Token          | Value   | Usage                |
| -------------- | ------- | -------------------- |
| Background     | #FFFFFF | Main page background |
| Surface        | #F5F5F5 | Cards and panels     |
| Border         | #E0E0E0 | Dividers             |
| Text Primary   | #111111 | Main text            |
| Text Secondary | #555555 | Supporting text      |
| Text Tertiary  | #999999 | Labels               |
| Accent         | #FF6B00 | CTA buttons          |
| Accent Hover   | #FF9040 | Hover state          |
| Success        | #16A34A | Free delivery        |

---

# Typography

## Display Font

**Space Grotesk**

Used for:

* Body
* Descriptions
* Supporting copy

---

## Technical Font

**JetBrains Mono**

Used for:

* Headings
* Prices
* Buttons
* Labels
* Specs
* Badges

---

# Border Radius

Very subtle.

```css
2px – cards
2px – buttons
4px – large containers
```

The UI should feel almost square and technical.

---

# Spacing System

```text
4px
8px
12px
16px
24px
32px
48px
64px
```

Generous whitespace is preferred.

---

# Background Style

The application uses a subtle dot grid.

```css
radial-gradient(
circle,
rgba(0,0,0,0.1) 1px,
transparent 1px
)
```

Grid size:

```text
24px × 24px
```

---

# Layout Structure

## Page Hierarchy

```text
Top Navigation
↓
Hero Section
↓
Top Picks
↓
Filter Panel
↓
Search Results
↓
Product Detail Page
↓
Modals
```

---

# 1. Top Navigation

Height:

```text
64px
```

Contains:

* MakeDo logo
* Upgrade button
* City selector

Style:

```text
Sticky
Semi-transparent white
Backdrop blur
Bottom border
```

---

# 2. Hero Section

Purpose:

Help users understand the product immediately.

Content:

* Eyebrow text
* Large headline
* Supporting paragraph
* Primary CTA
* Secondary CTA

---

## Hero Title

Desktop:

```text
36px
```

Mobile:

```text
22px
```

Uppercase.

Monospace.

---

## Primary CTA

Background:

```text
#FF6B00
```

Hover:

```text
#FF9040
```

Micro interaction:

```text
translateY(-2px)
```

---

# 3. Top Picks Section

Purpose:

Show popular laptops immediately.

Layout:

```text
Responsive card grid
```

Desktop:

```text
5–6 columns
```

Tablet:

```text
3 columns
```

Mobile:

```text
2 columns
```

---

# Laptop Card

Structure:

```text
Image
↓
Brand
↓
Laptop Name
↓
Specs
↓
Price + Action
```

---

## Card States

### Default

White background.

### Hover

```css
background: #f9f9f9;
```

### Selected

Accent border.

---

# 4. Filter Panel

Purpose:

Guide users toward laptops that match their workflow.

Sections:

1. Department
2. Applications

---

## Department Buttons

Style:

* Outline
* Technical
* Monospace
* Icon + Label

Selected:

```text
Orange border
Orange text
Light orange background
```

---

## App Pills

Small pills.

Can be multi-selected.

---

# 5. Results Page

Layout:

```text
3-column responsive grid
```

Cards are larger than Top Picks.

---

# Product Card Structure

```text
Image
Badge
Free Delivery Pill
Brand
Name
Specs
Price
View Button
```

---

## Compatibility Badges

### Perfect Match

White outline.

### Good Match

Orange.

### Usable

Gray.

### Unrated

Dark gray.

---

# 6. Product Detail Page (PDP)

Desktop:

```text
2 columns
```

Left:

* Product image
* Performance breakdown

Right:

* Product information
* Price
* Specifications
* CTA buttons

---

# Performance Card

Purpose:

Show how well the laptop performs for selected applications.

Visualization:

```text
Application Name
██████████
Score
```

Animation:

```css
transition: width .8s ease;
```

---

# Primary Actions

## Buy Button

```text
Black background
White text
```

---

## AR Preview Button

```text
Orange outline
Transparent background
```

---

# 7. AR Modal

Purpose:

Preview laptop in physical space.

Modal Width:

```text
440px
```

Background:

```text
rgba(0,0,0,.88)
```

---

# 8. Upgrade Modal

Purpose:

Convince users to benchmark and upgrade their current PC before purchasing.

Flow:

1. Download
2. Stress Test
3. Receive Upgrade Plan

---

# Interaction Principles

## Motion

Subtle.

Fast.

No excessive animation.

---

## Hover Effects

* Border color changes
* Background tint
* Slight elevation
* 150–200ms duration

---

# Responsive Design

## Mobile (<700px)

Changes:

* Single-column PDP
* Smaller padding
* Two-column product grids
* Reduced hero spacing

---

# Accessibility

## Contrast

Minimum:

```text
4.5:1
```

---

## Interactive Targets

Minimum:

```text
44px × 44px
```

---

## Keyboard Support

* Tab navigation
* Focus states
* Escape closes modals

---

# Future Features

## V2

* AI recommendations
* Saved configurations
* Compare laptops
* Wishlist
* Benchmark uploads
* Financing calculator
* Delivery ETA
* User reviews
* Availability by city
* Trade-in program
* Local AI workload estimator

---

# Design Goal

The user should feel:

> "This website understands exactly what work I do and can confidently recommend the right laptop for me."
