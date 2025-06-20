package org.packages.GUI;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;
import java.util.List;

import org.packages.diskDrives.diskCheck;
import org.packages.CPU.cpuCheck;
import org.packages.GPU.gpuCheck;
import org.packages.RAM.ramCheck;
import org.packages.battery.batteryCheck;
import org.packages.screen.screenCheck;
import org.packages.OS.osCheck; 
import org.packages.peripherals.peripheralsCheck;// <-- Added
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.UsbDevice;
import oshi.SystemInfo;


class signInPage{
    public void showSignInPage(){

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf.");
        }

        JFrame signInWindow = new JFrame("MakeDo - Sign in");
        signInWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        signInWindow.setSize(325, 475);

        JPanel signInPanel = new JPanel(new GridBagLayout());
        GridBagConstraints position = new GridBagConstraints();
        position.fill = GridBagConstraints.NONE;
        position.gridy = 0;
        position.gridx = 0;
        position.weightx = 0;
        position.insets = new Insets(1, 1, 0,1);
        position.anchor = GridBagConstraints.CENTER;


        ImageIcon signIN_img = new ImageIcon("C:\\Users\\boni\\Desktop\\Files\\MakeDo\\assets\\user-light.png");
        Image scaledImage = signIN_img.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH);
        JLabel signIN_imgLabel = new JLabel(new ImageIcon(scaledImage));
        signInPanel.add(signIN_imgLabel);


        JLabel spacer = new JLabel("      ");
        position.gridy = 0;
        signInPanel.add(spacer, position);

        JLabel spacer2 = new JLabel("      ");
        position.gridy = 1;
        signInPanel.add(spacer2, position);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 9, 9));

        JButton loginBtn = new JButton("Log In");
        loginBtn.setFont(new Font("Arial", Font.BOLD, 12));
        loginBtn.addActionListener(e -> {
            userLogin userLogin = new userLogin();
            userLogin.showUserLogin();
            signInWindow.setVisible(false);
        });
        JLabel slashTxt = new JLabel("or");
        JButton signUpBtn = new JButton("Sign Up");
        signUpBtn.setFont(new Font("Arial", Font.BOLD, 12));
        signUpBtn.addActionListener(e -> {
            newSignUp signUp = new newSignUp();
            signUp.showNewSignUp();
            signInWindow.setVisible(false);
        });

        loginBtn.setBackground(Color.lightGray);
        signUpBtn.setBackground(Color.lightGray);

        buttonsPanel.add(loginBtn);
        buttonsPanel.add(slashTxt);
        buttonsPanel.add(signUpBtn);

        position.gridy = 2;
        signInPanel.add(buttonsPanel, position);

        JLabel dash = new JLabel("               ");
        position.gridy = 3;
        signInPanel.add(dash, position);


        JButton contAsGuest = new JButton("Continue as guest");
        contAsGuest.setBackground(Color.lightGray);
        contAsGuest.setFont(new Font("Arial", Font.BOLD, 10));
        contAsGuest.setPreferredSize(new Dimension(175,15));
        signInPanel.add(contAsGuest, position);

        Font fontMod = new Font("Arial", Font.PLAIN, 11);
        UIManager.put("OptionPane.messageFont", fontMod);

        contAsGuest.addActionListener(e ->
        {   int result = JOptionPane.showConfirmDialog(
                null,
                "You CAN'T sell or make purchases as a GUEST . \n             Do you want to proceed?",
                "Continue as Guest",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
            if(result == JOptionPane.YES_OPTION){
                mainPage mainPage = new mainPage();
                mainPage.showMainPage();
            }else if(result == JOptionPane.NO_OPTION){
                showSignInPage();
            }
            signInWindow.setVisible(false);
        });

        Color originalBg = UIManager.getColor("Button.background");
        UIManager.put("Button.background", Color.LIGHT_GRAY);


        JPanel spacedOut = new JPanel(new FlowLayout(FlowLayout.CENTER, 1, 40));

        JLabel lang = new JLabel("Choose language ");
        lang.setFont(new Font("Arial", Font.PLAIN, 10));

        String[] languages = {"English", "Amharic"};
        JComboBox<String> langDropDown = new JComboBox<>(languages);
        langDropDown.setSelectedIndex(0);
        langDropDown.setPreferredSize(new Dimension(70, 18));
        langDropDown.setBackground(Color.lightGray);
        langDropDown.setForeground(Color.BLACK);
        langDropDown.setFont(new Font("Arial", Font.PLAIN, 11));

        spacedOut.add(lang);
        spacedOut.add(langDropDown);
        position.gridy= 4;
        signInPanel.add(spacedOut, position);

        JPanel spacedCombo = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
        JLabel nothing = new JLabel("   ");

        JLabel devCredit = new JLabel("Developed by Bonsa Dereje");
        devCredit.setFont(new Font("Arial", Font.PLAIN, 7));


        spacedCombo.add(nothing);
        spacedCombo.add(devCredit);
        position.gridy = 5;
        signInPanel.add(spacedCombo, position);



        signInWindow.add(signInPanel);
        signInWindow.setVisible(true);
        signInWindow.setLocationRelativeTo(null);
    }
}
class userLogin{
    public void showUserLogin(){
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf.");
        }

        JFrame loginPage = new JFrame("MakeDo - Log in");
        loginPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginPage.setSize(350, 500);


        JPanel logInPanel = new JPanel(new GridBagLayout());
        GridBagConstraints position = new GridBagConstraints();
        position.fill = GridBagConstraints.NONE;
        position.gridy = 0;
        position.gridx = 0;
        position.weightx = 0;
        position.insets = new Insets(1, 1, 0,1);
        position.anchor = GridBagConstraints.WEST;

        JPanel subPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 7));

        JLabel login = new JLabel("Login");
        login.setFont(new Font("Arial", Font.BOLD, 25));

        JLabel space = new JLabel(" ");

        ImageIcon loginImg = new ImageIcon("C:\\Users\\boni\\Desktop\\Files\\MakeDo\\assets\\login.png");
        Image scaledImage = loginImg.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        JLabel signIN_imgLabel = new JLabel(new ImageIcon(scaledImage));

        subPanel.add(login);
        subPanel.add(space);
        subPanel.add(signIN_imgLabel);
        position.gridy = 0;
        logInPanel.add(subPanel, position);


        JLabel userName = new JLabel("Full Name");
        userName.setFont(new Font("Arial", Font.PLAIN, 11));
        position.gridy= 1;
        logInPanel.add(userName, position);


        JTextField userNameIn = new JTextField(20);
        position.gridy = 2;
        logInPanel.add(userNameIn, position);

        JLabel password = new JLabel("Password");
        password.setFont(new Font("Arial", Font.PLAIN, 11));
        position.gridy= 3;
        logInPanel.add(password, position);


        JPasswordField passwordIn = new JPasswordField(20);
        position.gridy = 4;
        logInPanel.add(passwordIn, position);
        position.anchor = GridBagConstraints.EAST;
        JButton forgotPass = new JButton("Forgot Password?");
        forgotPass.setFont(new Font("Arial", Font.PLAIN, 8));
        forgotPass.setPreferredSize(new Dimension(70, 10));
        forgotPass.setContentAreaFilled(false);
        forgotPass.setBorderPainted(false);
        forgotPass.setMargin(new Insets(0, 0, 0, 0));
        position.gridy = 5;
        logInPanel.add(forgotPass, position);
/*
        position.gridy = 0;
        logInPanel.add(subPanel, position);

 */
        JPanel credit = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 50));

        JLabel space2 = new JLabel(" ");
        position.gridy = 6;
        logInPanel.add(space2, position);

        position.anchor = GridBagConstraints.CENTER;
        JButton proceedLogin = new JButton("Login");
        proceedLogin.setBackground(Color.lightGray);
        proceedLogin.setFocusPainted(false);
        position.gridy = 7;
        logInPanel.add(proceedLogin, position);

        JPanel switchToLogin = new JPanel(new FlowLayout(FlowLayout.CENTER, 1, 3));

        JLabel dontHave = new JLabel("Don't have an account?");
        dontHave.setFont(new Font("Arial", Font.PLAIN, 9));

        JButton signUpInstead = new JButton("Sign Up");
        signUpInstead.setFont(new Font("Arial", Font.BOLD, 11));
        signUpInstead.setContentAreaFilled(false);
        signUpInstead.setPreferredSize(new Dimension(47, 15));
        signUpInstead.setBorderPainted(false);
        signUpInstead.addActionListener(e -> {
            newSignUp signUp = new newSignUp();
            signUp.showNewSignUp();
            loginPage.setVisible(false);
        });
        signUpInstead.setMargin(new Insets(0, 0, 0, 0));

        switchToLogin.add(dontHave);
        switchToLogin.add(signUpInstead);
        position.gridy = 8;
        logInPanel.add(switchToLogin, position);


        JLabel nothing = new JLabel("   ");

        JPanel spacer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 6));
        spacer.add(nothing);
        position.gridy = 9;
        logInPanel.add(spacer, position);


        loginPage.add(logInPanel);
        loginPage.setVisible(true);
        loginPage.setLocationRelativeTo(null);
    }
}

class newSignUp{
    public void showNewSignUp(){

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf.");
        }

        JFrame signUpPage = new JFrame("MakeDo - Sign-Up");
        signUpPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        signUpPage.setSize(350, 500);


        JPanel signUpPanel = new JPanel(new GridBagLayout());
        GridBagConstraints position = new GridBagConstraints();
        position.fill = GridBagConstraints.NONE;
        position.gridy = 0;
        position.gridx = 0;
        position.weightx = 0;
        position.insets = new Insets(1, 1, 0,1);
        position.anchor = GridBagConstraints.WEST;

        JPanel subPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 7));

        JLabel login = new JLabel("SignUp");
        login.setFont(new Font("Arial", Font.BOLD, 25));

        JLabel space = new JLabel("  ");

        ImageIcon loginImg = new ImageIcon("C:\\Users\\boni\\Desktop\\Files\\MakeDo\\assets\\add-user.png");
        Image scaledImage = loginImg.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        JLabel signIN_imgLabel = new JLabel(new ImageIcon(scaledImage));

        subPanel.add(login);
        subPanel.add(space);
        subPanel.add(signIN_imgLabel);
        position.gridy = 0;
        signUpPanel.add(subPanel, position);


        JLabel userName = new JLabel("Full Name");
        userName.setFont(new Font("Arial", Font.PLAIN, 11));
        position.gridy= 1;
            signUpPanel.add(userName, position);
        JTextField userNameIn = new JTextField(20);
        position.gridy = 2;
            signUpPanel.add(userNameIn, position);


        JLabel address = new JLabel("Address(City)");
        address.setFont(new Font("Arial", Font.PLAIN, 11));
        position.gridy= 3;
            signUpPanel.add(address, position);
        JTextField addressIn = new JTextField(20);
        position.gridy = 4;
        signUpPanel.add(addressIn, position);

        JLabel idNo = new JLabel("National ID No");
        idNo.setFont(new Font("Arial", Font.PLAIN, 11));
        position.gridy= 5;
            signUpPanel.add(idNo, position);
        JTextField idNoIn = new JTextField(20);
        position.gridy = 6;
            signUpPanel.add(idNoIn, position);

        JLabel phoneNo = new JLabel("Phone Number");
        phoneNo.setFont(new Font("Arial", Font.PLAIN, 11));
        position.gridy= 7;
            signUpPanel.add(phoneNo, position);
        JTextField phoneNoIn = new JTextField(20);
        position.gridy = 8;
            signUpPanel.add(phoneNoIn, position);

        JLabel password = new JLabel("Password");
        password.setFont(new Font("Arial", Font.PLAIN, 11));
        position.gridy= 9;
            signUpPanel.add(password, position);
        JPasswordField passwordIn = new JPasswordField(20);
        position.gridy = 10;
            signUpPanel.add(passwordIn, position);

        JLabel confirmPassword = new JLabel("Confirm Password");
        confirmPassword.setFont(new Font("Arial", Font.PLAIN, 11));
        position.gridy= 11;
            signUpPanel.add(confirmPassword, position);
        JPasswordField confrimPasswordIn = new JPasswordField(20);
        position.gridy = 12;
            signUpPanel.add(confrimPasswordIn, position);

        JLabel space2 = new JLabel(" ");
        position.gridy = 13;
            signUpPanel.add(space2, position);

        position.anchor = GridBagConstraints.CENTER;
        JButton proceedLogin = new JButton("Sign UP");
        proceedLogin.setBackground(Color.lightGray);
        proceedLogin.setFocusPainted(false);
        position.gridy = 14;
        signUpPanel.add(proceedLogin, position);

        JPanel switchToLogin = new JPanel(new FlowLayout(FlowLayout.CENTER, 1, 3));

        JLabel alreadyHave = new JLabel("Already have an account?");
        alreadyHave.setFont(new Font("Arial", Font.PLAIN, 9));

        JButton signUpInstead = new JButton("Log In");
        signUpInstead.setFont(new Font("Arial", Font.BOLD, 11));
        signUpInstead.setContentAreaFilled(false);
        signUpInstead.setPreferredSize(new Dimension(47, 15));
        signUpInstead.setBorderPainted(false);
        signUpInstead.addActionListener(e -> {
            userLogin userLogin = new userLogin();
            userLogin.showUserLogin();
            signUpInstead.setVisible(false);
        });
        signUpInstead.setMargin(new Insets(0, 0, 0, 0));

        switchToLogin.add(alreadyHave);
        switchToLogin.add(signUpInstead);
        position.gridy = 15;
        signUpPanel.add(switchToLogin, position);

        signUpPage.add(signUpPanel);
        signUpPage.setVisible(true);
        signUpPage.setLocationRelativeTo(null);
    }
}


class mainPage{
    public void showMainPage(){
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf.");
        }
        JFrame mainWindow = new JFrame("MakeDo");
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setSize(350,500);

        JPanel mainPanel = new JPanel( new GridBagLayout());
        GridBagConstraints position = new GridBagConstraints();
        position.fill = GridBagConstraints.NONE;
        position.gridx= 0;
        position.gridy =0;
        position.weightx =0;
        position.insets = new Insets(1, 1, 1,1);
        //mainPanel.setLayout(new BoxLayout((mainPanel, BoxLayout.Y_AXIS)));
        


        position.anchor = GridBagConstraints.CENTER;
        JLabel appName = new JLabel("MakeDo");
        position.gridx = 0;
        position.gridy = 0;
        appName.setFont(new Font("Arial", Font.PLAIN,25));
        mainPanel.add(appName, position);


        JLabel appDesc = new JLabel("a comprehensive pc stat and benchmarking software");
        position.gridx = 0;
        position.gridy = 1;
        appDesc.setFont(new Font("Arial", Font.PLAIN,12));
        mainPanel.add(appDesc, position);

        JLabel br = new JLabel("             ");
        position.gridx = 0;
        position.gridy = 2;
        //mainPanel.add(br, position);

        JCheckBox hardDisk = new JCheckBox("HardDisk ");
        hardDisk.setOpaque(true);
        JCheckBox cpu = new JCheckBox("CPU ");
        JCheckBox ram = new JCheckBox("RAM ");
        JCheckBox os = new JCheckBox("OS ");
        JCheckBox screen = new JCheckBox("Screen ");
        JCheckBox battery = new JCheckBox("Battery ");
        JCheckBox peripherals = new JCheckBox("Peripherals");
        JCheckBox gpu = new JCheckBox("GPU ");

        position.gridy =3;

        hardDisk.setSelected(true);
        mainPanel.add(hardDisk, position);

        position.gridy = 4;
        cpu.setSelected(true);
        mainPanel.add(cpu, position);

        position.gridy = 5;
        ram.setSelected(true);
        mainPanel.add(ram,position);

        position.gridy = 6;
        os.setSelected(true);
        mainPanel.add(os, position);

        position.gridy = 7;
        screen.setSelected(true);
        mainPanel.add(screen, position);

        position. gridy = 8;
        battery.setSelected(true);
        mainPanel.add(battery, position);

        position.gridy =9;
        peripherals.setSelected(true);
        mainPanel.add(peripherals, position);

        position.gridy = 10;
        gpu.setSelected(true);
        mainPanel.add(gpu, position);


        JButton selectAll = new JButton("All");
        position.gridy = 11;
        selectAll.setPreferredSize(new Dimension(70,15));
        selectAll.setFont(new Font("Arial", Font.BOLD, 10));
        selectAll.setBackground(Color.lightGray);
        //mainPanel.add(selectAll, position);

        JLabel br3 = new JLabel("---------");
        position.gridy = 11;
        //mainPanel.add(br3, position);

        position.anchor = GridBagConstraints.CENTER;


        JButton clear = new JButton("Clear");
        position.gridy = 12;
        position.gridx = 0;
        clear.setPreferredSize(new Dimension(70,15));
        clear.setFont(new Font("Arial", Font.BOLD, 10));
        clear.setBackground(Color.lightGray);
        mainPanel.add(clear, position);

        clear.addActionListener(e -> {
            hardDisk.setSelected(false);
            cpu.setSelected(false);
            os.setSelected(false);
            ram.setSelected(false);
            screen.setSelected(false);
            battery.setSelected(false);
            peripherals.setSelected(false);
            gpu.setSelected(false);
        });


        JButton readSystemFiles = new JButton("Read System Files");
            position.gridy = 13;
        readSystemFiles.setFont(new Font("Arial", Font.PLAIN, 12));
        readSystemFiles.setOpaque(true);
        readSystemFiles.setBackground(Color.lightGray);
        readSystemFiles.setPreferredSize(new Dimension(150, 25));
        //mainPanel.add(readSystemFiles, position);

        
    

         JLabel br2 = new JLabel("       ");
        position.gridy= 15;
        mainPanel.add(br2,position);


        JButton run = new JButton("RUN");
        position.gridx = 0;
        position.gridy = 16;
        run.setFont(new Font("Arial", Font.BOLD,14));
        run.setOpaque(true);
        run.setBackground(Color.lightGray);
        run.setPreferredSize(new Dimension(120,40));
        mainPanel.add(run, position);
        
       run.addActionListener(e -> {
    if (
        hardDisk.isSelected() &&
        cpu.isSelected() &&
        os.isSelected() &&
        ram.isSelected() &&
        screen.isSelected() &&
        battery.isSelected() &&
        peripherals.isSelected() &&
        gpu.isSelected()
    ) {
        System.out.println("All checkboxes are selected");

        run.setEnabled(false);
        JLabel readingSysInfo = new JLabel("Reading system info, please wait.");
        position.gridy = 17;
        mainPanel.add(readingSysInfo, position);
        mainPanel.revalidate();
        mainPanel.repaint();

        String[] dots = {".", "..", "..."};
        final int[] dotIndex = {0};

        Timer dotTimer = new Timer(400, ev -> {
            readingSysInfo.setText("Reading system info, please wait" + dots[dotIndex[0]]);
            dotIndex[0] = (dotIndex[0] + 1) % dots.length;
        });
        dotTimer.start();

        //hardDisk.setForeground(Color.GREEN);

        Timer delayTimer = new Timer(3000, event -> {
            dotTimer.stop();
            mainDashboard dashboard = new mainDashboard();
            dashboard.showMainDashboard();
            mainWindow.setVisible(false);
        });

        delayTimer.setRepeats(false);
        delayTimer.start();
    }
});

    
    


        for(FileStore store : FileSystems.getDefault().getFileStores()){
            try {
                System.out.println("Drive: " + store.name());
                double total = store.getTotalSpace();
                double free = store.getUsableSpace();
                double used = total - free;

                System.out.println("Total: " + total + " GB");
                System.out.println("Used " + used + " GB" );
                System.out.println("Free: "+ free + " GB");
            } catch (Exception e){
                System.err.println("Error reading partition: " + e.getMessage());
            }
        }
        

        JFrame basicInfo = new JFrame("Leap");



        mainWindow.add(mainPanel);
        mainWindow.setVisible(true);
        mainWindow.setLocationRelativeTo(null);

    }
}


class mainDashboard {

    static class RoundedPanel extends JPanel {
        private int radius;

        public RoundedPanel(int radius) {
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public void showMainDashboard() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf.");
        }

        JFrame mainDashboardWindow = new JFrame("MakeDo - Dashboard");
        //mainDashboardWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainDashboardWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainDashboardWindow.setLayout(new FlowLayout());
        mainDashboardWindow.setSize(1000, 650);
        mainDashboardWindow.setLocationRelativeTo(mainDashboardWindow);

        JPanel mainDash = new JPanel(new FlowLayout(FlowLayout.LEFT, 7, 5));
        JPanel mainSubPanel = new JPanel();
        mainSubPanel.setLayout(new BoxLayout(mainSubPanel, BoxLayout.Y_AXIS));
        mainSubPanel.add(Box.createVerticalStrut(15));

        JPanel slit1 = new JPanel();
        slit1.setLayout(new BoxLayout(slit1, BoxLayout.Y_AXIS));

        JPanel slit2 = new JPanel();
        slit2.setLayout(new BoxLayout(slit2, BoxLayout.Y_AXIS));

        JPanel slit3 = new JPanel();
        slit3.setLayout(new BoxLayout(slit3, BoxLayout.Y_AXIS));


        JPanel hrSubPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 3));
        JPanel hddDash = new RoundedPanel(20);
        hddDash.setLayout(new BoxLayout(hddDash, BoxLayout.Y_AXIS));
        hddDash.setBackground(Color.decode("#B0C4DE"));
        hddDash.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel harddiskTtl = new JLabel("Hard Disk");
        harddiskTtl.setFont(new Font("Roboto", Font.BOLD, 15));
        hddDash.add(Box.createVerticalStrut(2));

        ImageIcon hddImg = new ImageIcon("G:\\Dev Softwares\\MakeDo\\assets\\hard-disk-drive.png");
        Image hddScaledImage = hddImg.getImage().getScaledInstance(23, 23, Image.SCALE_SMOOTH);
        JLabel hdd_imgLabel = new JLabel(new ImageIcon(hddScaledImage));

        hrSubPanel.add(harddiskTtl);
        hrSubPanel.add(hdd_imgLabel);
        hrSubPanel.setOpaque(false);
        hddDash.add(hrSubPanel);

        UIManager.put("Label.font", new Font("Roboto", Font.PLAIN, 13));

        List<diskCheck.DriveInfo> drives = diskCheck.scanAllDrives();
        for (int i = 0; i < drives.size(); i++) {
            diskCheck.DriveInfo drive = drives.get(i);
            hddDash.add(Box.createVerticalStrut(5));

            hddDash.add(new JLabel("Drive: " + drive.name));
            hddDash.add(new JLabel("Health: " + drive.health + " / 100"));
            hddDash.add(new JLabel("Bad Sectors: " + drive.badSectors));
/*
            if (!drive.issues.isEmpty()) {
                for (String issue : drive.issues) {
                    String cleaned = issue.replaceAll("\\s*\\(.*?\\)", "").trim();
                    hddDash.add(new JLabel(cleaned));
                }
            }
 */
            if (!drive.partitions.isEmpty()) {
                hddDash.add(new JLabel("Partitions:"));
                for (diskCheck.PartitionInfo p : drive.partitions) {
                    String partInfo = String.format("%.2f GB total",
                            p.totalGB);
                    hddDash.add(new JLabel(partInfo));
                }
            }

            if (i < drives.size() - 1) {
                hddDash.add(Box.createVerticalStrut(10));
                hddDash.add(new JSeparator(SwingConstants.HORIZONTAL));
            }
        }


        JPanel cpuDash = new RoundedPanel(20);
        cpuDash.setLayout(new BoxLayout(cpuDash, BoxLayout.Y_AXIS));
        cpuDash.setBackground(Color.decode("#B0C4DE"));
        cpuDash.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel cpuTtlSub = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 3));
        cpuTtlSub.setOpaque(false);

        JLabel cpuTtl = new JLabel("CPU");
        cpuTtl.setFont(new Font("Roboto", Font.BOLD, 15));

        ImageIcon cpuImg = new ImageIcon("C:\\Users\\boni\\Desktop\\Files\\MakeDo\\assets\\processor.png");
        Image cpuScaledImage = cpuImg.getImage().getScaledInstance(23, 23, Image.SCALE_SMOOTH);
        JLabel cpu_imgLabel = new JLabel(new ImageIcon(cpuScaledImage));

        cpuTtlSub.add(cpuTtl);
        cpuTtlSub.add(cpu_imgLabel);
        cpuDash.add(cpuTtlSub);

        cpuCheck.CPUInfo cpuInfo = cpuCheck.getCPUInfo();

        cpuDash.add(Box.createVerticalStrut(5));
        cpuDash.add(new JLabel("Model: " + cpuInfo.processorName));
        cpuDash.add(new JLabel("Vendor: " + cpuInfo.vendor));
        cpuDash.add(new JLabel("Identifier: " + cpuInfo.identifier));
        cpuDash.add(new JLabel("Microarchitecture: " + cpuInfo.microarchitecture));
        cpuDash.add(new JLabel("Physical Cores: " + cpuInfo.physicalCores));
        cpuDash.add(new JLabel("Logical Cores: " + cpuInfo.logicalCores));
        cpuDash.add(new JLabel(String.format("Max Frequency: %.2f GHz", cpuInfo.maxFreqHz / 1_000_000_000.0)));

        // ---------- RAM Panel ----------
        JPanel ramDash = new RoundedPanel(20);
        ramDash.setLayout(new BoxLayout(ramDash, BoxLayout.Y_AXIS));
        ramDash.setBackground(Color.decode("#B0C4DE"));
        ramDash.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel ramTtlSub = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 3));
        ramTtlSub.setOpaque(false);

        JLabel ramTtl = new JLabel("RAM");
        ramTtl.setFont(new Font("Roboto", Font.BOLD, 15));

        ImageIcon ramImg = new ImageIcon("C:\\Users\\boni\\Desktop\\Files\\MakeDo\\assets\\ram.png");
        Image ramScaledImage = ramImg.getImage().getScaledInstance(23, 23, Image.SCALE_SMOOTH);
        JLabel ram_imgLabel = new JLabel(new ImageIcon(ramScaledImage));

        ramTtlSub.add(ramTtl);
        ramTtlSub.add(ram_imgLabel);
        ramDash.add(ramTtlSub);

        ramCheck.RAMInfo ramInfo = ramCheck.getRAMInfo();
        ramDash.add(Box.createVerticalStrut(5));
        ramDash.add(new JLabel(String.format("Total Memory: %.2f GB", ramInfo.totalMemoryGB)));
        ramDash.add(new JLabel(String.format("Available Memory: %.2f GB", ramInfo.availableMemoryGB)));
        ramDash.add(new JLabel("RAM Slots Used: " + ramInfo.modules.size()));
        ramDash.add(new JLabel("Estimated Vacant Slots: " + ramInfo.estimatedSlots));

        for (int i = 0; i < ramInfo.modules.size(); i++) {
            ramDash.add(Box.createVerticalStrut(5));
            ramCheck.RAMInfo.Module m = ramInfo.modules.get(i);
            ramDash.add(new JLabel("Module " + (i + 1) + ":"));
            ramDash.add(new JLabel("  Bank Label: " + m.bankLabel));
            ramDash.add(new JLabel(String.format("  Capacity: %.2f GB", m.capacityGB)));
            ramDash.add(new JLabel("  Manufacturer: " + m.manufacturer));
            ramDash.add(new JLabel("  Memory Type: " + m.memoryType));
            ramDash.add(new JLabel("  Clock Speed: " + m.clockSpeedMHz));
            ramDash.add(new JLabel("  Transfer Rate: " + m.transferRateMTps));
        }

        JPanel sideStack = new JPanel(new FlowLayout());


        JPanel batteryDash = new RoundedPanel(20);
        batteryDash.setLayout(new BoxLayout(batteryDash, BoxLayout.Y_AXIS));
        batteryDash.setBackground(Color.decode("#B0C4DE"));
        batteryDash.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel batteryTtlSub = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 3));
        batteryTtlSub.setOpaque(false);

        JLabel batteryTtl = new JLabel("Battery");
        batteryTtl.setFont(new Font("Roboto", Font.BOLD, 15));

        ImageIcon batteryImg = new ImageIcon("C:\\Users\\boni\\Desktop\\Files\\MakeDo\\assets\\battery.png");
        Image batteryScaledImage = batteryImg.getImage().getScaledInstance(23, 23, Image.SCALE_SMOOTH);
        JLabel battery_imgLabel = new JLabel(new ImageIcon(batteryScaledImage));

        batteryTtlSub.add(batteryTtl);
        batteryTtlSub.add(battery_imgLabel);
        batteryDash.add(batteryTtlSub);

        List<batteryCheck.BatteryInfo> batteryInfoList = batteryCheck.getBatteryInfo();
        if (!batteryInfoList.isEmpty()) {
            batteryCheck.BatteryInfo batteryInfo = batteryInfoList.get(0);
            batteryDash.add(Box.createVerticalStrut(5));
            batteryDash.add(new JLabel("Name: " + batteryInfo.name));
            batteryDash.add(new JLabel("Voltage: " + batteryInfo.voltage + " V"));
            batteryDash.add(new JLabel("Design Capacity (Wh): " + batteryInfo.designCapacityWh));
            batteryDash.add(new JLabel("Max Capacity (Wh): " + batteryInfo.maxCapacityWh));
            batteryDash.add(new JLabel("Current Capacity (Wh): " + batteryInfo.currentCapacityWh));
        } else {
            batteryDash.add(Box.createVerticalStrut(5));
            batteryDash.add(new JLabel("No battery detected."));
        }

        
        JPanel screenDash = new RoundedPanel(20);
        screenDash.setLayout(new BoxLayout(screenDash, BoxLayout.Y_AXIS));
        screenDash.setBackground(Color.decode("#B0C4DE"));
        screenDash.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel screenTtlSub = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 3));
        screenTtlSub.setOpaque(false);

        JLabel screenTtl = new JLabel("Screen");
        screenTtl.setFont(new Font("Roboto", Font.BOLD, 15));

        ImageIcon screenImg = new ImageIcon("C:\\Users\\boni\\Desktop\\Files\\MakeDo\\assets\\screen.png");
        Image screenScaledImage = screenImg.getImage().getScaledInstance(23, 23, Image.SCALE_SMOOTH);
        JLabel screen_imgLabel = new JLabel(new ImageIcon(screenScaledImage));

        screenTtlSub.add(screenTtl);
        screenTtlSub.add(screen_imgLabel);
        screenDash.add(screenTtlSub);

        screenCheck.ScreenInfo[] screens = screenCheck.getAllScreenInfo();
        if (screens.length == 0) {
            screenDash.add(Box.createVerticalStrut(5));
            screenDash.add(new JLabel("No screens detected."));
        } else {
            for (screenCheck.ScreenInfo screen : screens) {
                screenDash.add(Box.createVerticalStrut(5));
                screenDash.add(new JLabel("Display " + screen.displayNumber + ":"));
                screenDash.add(new JLabel("  Resolution: " + screen.width + "x" + screen.height));
                screenDash.add(new JLabel("  Refresh Rate: " + screen.refreshRate + " Hz"));
                screenDash.add(new JLabel("  Bit Depth: " + screen.bitDepth));
                screenDash.add(new JLabel("  Pixel Size: " + screen.pixelSize + " bits"));
                screenDash.add(new JLabel("  Color Space: " + screen.colorSpaceName));
                screenDash.add(new JSeparator(SwingConstants.HORIZONTAL));
            }
        }

        
        JPanel osDash = new RoundedPanel(20);
        osDash.setLayout(new BoxLayout(osDash, BoxLayout.Y_AXIS));
        osDash.setBackground(Color.decode("#B0C4DE"));
        osDash.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel osTtlSub = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 3));
        osTtlSub.setOpaque(false);

        JLabel osTtl = new JLabel("OS");
        osTtl.setFont(new Font("Roboto", Font.BOLD, 15));

        ImageIcon osImg = new ImageIcon("C:\\Users\\boni\\Desktop\\Files\\MakeDo\\assets\\os.png");
        Image osScaledImage = osImg.getImage().getScaledInstance(23, 23, Image.SCALE_SMOOTH);
        JLabel os_imgLabel = new JLabel(new ImageIcon(osScaledImage));

        osTtlSub.add(osTtl);
        osTtlSub.add(os_imgLabel);
        osDash.add(osTtlSub);

        osCheck.OSInfo osInfo = osCheck.getOSInfo();
        osDash.add(Box.createVerticalStrut(5));
        osDash.add(new JLabel("Name: " + osInfo.name));
        osDash.add(new JLabel("Version: " + osInfo.version));
        osDash.add(new JLabel("Architecture: " + osInfo.architecture));
        osDash.add(new JLabel("Build Number: " + osInfo.buildNumber));


        JPanel gpuDash = new RoundedPanel(20);
        gpuDash.setLayout(new BoxLayout(gpuDash, BoxLayout.Y_AXIS));
        gpuDash.setBackground(Color.decode("#B0C4DE"));
        gpuDash.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel gpuTtlSub = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 3));
        gpuTtlSub.setOpaque(false);

        JLabel gpuTtl = new JLabel("GPU");
        gpuTtl.setFont(new Font("Roboto", Font.BOLD, 15));

        ImageIcon gpuImg = new ImageIcon("C:\\Users\\boni\\Desktop\\Files\\MakeDo\\assets\\gpu.png");
        Image gpuScaledImage = osImg.getImage().getScaledInstance(23, 23, Image.SCALE_SMOOTH);
        JLabel gpu_imgLabel = new JLabel(new ImageIcon(gpuScaledImage));

        gpuTtlSub.add(gpuTtl);
        gpuTtlSub.add(gpu_imgLabel);
        gpuDash.add(gpuTtlSub);
        
        List<gpuCheck.GPUInfo> gpuInfoList = gpuCheck.getGPUInfo();

if (gpuInfoList.isEmpty()) {
    gpuDash.add(new JLabel("No GPU detected."));
} else {
    for (int i = 0; i < gpuInfoList.size(); i++) {
        gpuCheck.GPUInfo gpu = gpuInfoList.get(i);
        gpuDash.add(new JLabel("GPU " + (i + 1) + ":"));
        gpuDash.add(new JLabel("  Name        : " + gpu.name));
        gpuDash.add(new JLabel("  Vendor      : " + gpu.vendor));
        gpuDash.add(new JLabel("  VRAM        : " + (gpu.vramBytes / (1024 * 1024)) + " MB"));
        gpuDash.add(new JLabel("  Device ID   : " + gpu.deviceId));
        gpuDash.add(new JLabel("  Version Info: " + gpu.versionInfo));
        gpuDash.add(new JSeparator(SwingConstants.HORIZONTAL));
    }
}

    

        JPanel prfDash = new RoundedPanel(20);
        prfDash.setLayout(new BoxLayout(prfDash, BoxLayout.Y_AXIS));
        prfDash.setBackground(Color.decode("#B0C4DE"));
        prfDash.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel prfTtlSub = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 3));
        prfTtlSub.setOpaque(false);

        JLabel prfTtl = new JLabel("Peripherals");
        prfTtl.setFont(new Font("Roboto", Font.BOLD, 15));

        ImageIcon prfImg = new ImageIcon("C:\\Users\\boni\\Desktop\\Files\\MakeDo\\assets\\usb.png");
        Image prfScaledImage = prfImg.getImage().getScaledInstance(23, 23, Image.SCALE_SMOOTH);
        JLabel prf_imgLabel = new JLabel(new ImageIcon(prfScaledImage));

        prfTtlSub.add(prfTtl);
        prfTtlSub.add(prf_imgLabel);
        prfDash.add(prfTtlSub);
        

        peripheralsCheck.PeripheralSummary prfSummary = peripheralsCheck.getPeripheralSummary();

        prfDash.add(Box.createVerticalStrut(5));
        prfDash.add(new JLabel("Estimated Physical USB Ports in Use: " + prfSummary.totalUsbDevices));
        prfDash.add(Box.createVerticalStrut(5));

        for (peripheralsCheck.PeripheralInfo device : prfSummary.devices) {
                String statusText = device.isWorking ? "Working" : "Unknown Status";
                prfDash.add(new JLabel(" - " + device.name + " [" + statusText + "]"));
            }
        slit1.add(hddDash);

        slit2.add(screenDash);
            sideStack.add(screenDash);
            sideStack.add(batteryDash);
        slit2.add(sideStack);
        slit2.add(gpuDash);

        slit3.add(ramDash);
        slit3.add(osDash);

        /*
        mainDash.add(hddDash);
        mainDash.add(cpuDash);
        mainDash.add(ramDash);
        mainDash.add(batteryDash);
        mainDash.add(screenDash);
        mainDash.add(osDash);
        mainDash.add(gpuDash);
        mainDash.add(prfDash);
         */

         mainDash.add(slit1);
         mainDash.add(slit2);
         mainDash.add(slit3);
         
         
         

        mainSubPanel.add(mainDash);
        mainDashboardWindow.add(mainSubPanel);
        mainDashboardWindow.setVisible(true);
    }
}


class PCTester{
    public static void main(String[] args){
        signInPage signIn = new signInPage();
        signIn.showSignInPage();
        userLogin login = new userLogin();
        //login.showUserLogin();
        mainPage mainpage = new mainPage();
        //mainpage.showMainPage();
        mainDashboard dashboard = new mainDashboard();
        //dashboard.showMainDashboard();



    }
}

