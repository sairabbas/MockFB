import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class View{

    private Database model;
    private ProfileManager manager;
    private JPanel mainPanel;
    private JPanel searchPanel;
    private JPanel friendListPanel; //A panel to hold friend's list

    /**
     * Constructor builds the panels and labels, has database in order to display.
     * Runs frame when object is created.
     * @param model holds the data
     * */
    public View(Database model){
        this.model = model;
        this.manager = new ProfileManager();
        int WINDOW_WIDTH = 780;
        int WINDOW_HEIGHT = 400;
        JLabel label = new JLabel("Friends List");

        //Create frame that takes button method
        JFrame frame = new HandleActionEventsForJButton();

        //Build panels
        buildFriendAndMainPanel();
        buildSearchPanel();

        //Box for friend list
        Box bv = Box.createVerticalBox();
        bv.add(label);
        bv.add(friendListPanel);

        //Add components to frame
        frame.add(bv, BorderLayout.EAST);
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(searchPanel, BorderLayout.SOUTH);

        //Display the window
        frame.pack();
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void LoginView() {
        final int WINDOW_WIDTH = 750;
        final int WINDOW_HEIGHT = 750;
        //Create a login window
        JFrame loginWindow = new JFrame();
        //Set the title
        loginWindow.setTitle("MockFB");
        //Set the size of the window
        loginWindow.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        //Specify what when the close button is clicked
        loginWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Create login instructions label
        JLabel instructionLabel = new JLabel("Create A Profile To Join Network");
        instructionLabel.setBounds(270,150,215,20);
        //Create name label
        JLabel nameLabel = new JLabel("Name");
        nameLabel.setBounds(270,200,80,25);
        //Create login text field 10 characters wide
        JTextField nameTextField = new JTextField(10);
        nameTextField.setBounds(308,200,165,25);
        //Create image label
        JLabel imageLabel = new JLabel("Select Image:");
        imageLabel.setBounds(270, 228, 175, 25);
        //Create login button with caption
        JButton joinButton = new JButton("Join");
        joinButton.setBounds(270, 275, 100, 25);
        //Create select image file button
        JFileChooser fileButton = new JFileChooser();
        fileButton.setBounds(370, 228, 700, 700);
        //Create JPanel object to reference labels
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(null);
        //Add labels to panel
        loginPanel.add(instructionLabel);
        loginPanel.add(nameLabel);
        loginPanel.add(nameTextField);
        loginPanel.add(imageLabel);
        loginPanel.add(joinButton);
        loginPanel.add(fileButton);
        //Add panel to login window
        loginWindow.add(loginPanel);
        //Display the login window
        loginWindow.setVisible(true);
    }


    public void buildSearchPanel(){
        searchPanel = new JPanel(new FlowLayout());
        mainPanel = new JPanel(new FlowLayout());
        JLabel searchLabel = new JLabel("Search:");
        JTextField searchText = new JTextField(10);

        //Match label with text
        searchLabel.setLabelFor(searchText);

        //Add label and text to panel
        searchPanel.add(searchLabel, FlowLayout.LEFT);
        searchPanel.add(searchText);

        //Create button
        JButton button = new JButton("Search");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = searchText.getText();            //Gets String of textfield
                Database d = manager.searchProfile(s);      //Gets profile of user and store it in Database object

                JLabel name = new JLabel(d.name);
                JLabel status = new JLabel(d.status);

                System.out.println(d.name + " " + d.status);
                System.out.println(s);

                //Remove all old data before adding new info to panel
                mainPanel.removeAll();

                mainPanel.add(name);
                mainPanel.add(status);

                mainPanel.revalidate();
                mainPanel.repaint();

            }
        });

        //Add button to panel
        searchPanel.add(button);

    }

    /**
     * Shows the friends list and aligns it downward
     * */
    public void buildFriendAndMainPanel(){
        friendListPanel = new JPanel();
        mainPanel = new JPanel(new BorderLayout());

        buildFriends();                             //Gets a sample of friends
        ArrayList<Database> f = model.getFriends(); //List of user's friends
        //TODO:Make every friend a button
        Box bv = Box.createVerticalBox();       //Create box for friend list
        Box bv2 = Box.createVerticalBox();      //Create box friend's friend list
        JButton button;                         //Button for friend name
        /**
         * When friend button is pressed, it gives adds their info into a JPanel.
         * The JPanel has name, status, and their friend's list
         * @param e the button pressed
         * */
        for (Database database : f) {                         //Each database obj is f(i)
            button = new JButton(database.getName());         //A button with a name
            bv.add(button);                                   //Adds button vertically
            button.addActionListener(new ActionListener() {
                /**
                 * When friend button is pressed, it gives adds their info into a JPanel.
                 * The JPanel has name, status, and their friend's list
                 * @param e the button pressed
                 * */
                @Override
                public void actionPerformed(ActionEvent e) {                     //Button pressed gives user info
                    String action = e.getActionCommand();
                    String friendName = manager.searchProfile(action).getName(); //Friend Name
                    String s = manager.searchProfile(action).getStatus();        //Friend status
                    int totalProfiles = manager.getProfilesCount();              //Total number of profiles

                    JLabel name = new JLabel("Name: " + friendName);        //Friend name label
                    JLabel status = new JLabel("Status: " + s + "\n");      //Friend status label

                    //Remove all old stuff in main panel
                    mainPanel.removeAll();

                    //Add to panel
                    mainPanel.add(name, BorderLayout.CENTER);
                    mainPanel.add(status, BorderLayout.CENTER);
                    mainPanel.revalidate();
                    mainPanel.repaint();

                    System.out.println("Currently viewing: " + friendName);

                    //Total number of friends your friend has
                    int friendTotal = manager.searchProfile(friendName).friendDatabases.size();

                    //Create friend's friend list
                    JLabel theirFriendList = new JLabel(friendName + "'s Friends List");
                    bv2.add(theirFriendList);

                    //Make button for every friend if they have any
                    if(friendTotal > 0){
                        for (int i = 0; i < friendTotal; i++) {
                            String friendsFriendName = manager.searchProfile(friendName).friendDatabases.get(i).getName();
                            JButton fButton =  new JButton(friendsFriendName);        //Button for friend's friend name
                            bv2.add(fButton);
                            System.out.println("Friend of Friend:" + friendsFriendName);
                        }
                    }
                    else
                        bv2.add(new JLabel("Currently has no friends."));
                }
            });
        }

        //TODO:When you click on a friend, show their info

        //Remove previous friend list panel
        friendListPanel.removeAll();

        //Add buttons to panel
        friendListPanel.add(bv);
        friendListPanel.revalidate();
        friendListPanel.repaint();

        mainPanel.add(bv2, BorderLayout.CENTER);
    }

    /**
     * Makes profiles and friends to test out program.
     * Currently idoes not use graphs to add/friend. Uses ArrayList.
     * */
    public void buildFriends(){
        //Make users
        Database d1 = new Database(); //Daniel
        Database d2 = new Database(); //Sair
        Database d3 = new Database(); //Josh
        Database d4 = new Database(); //Alexis

        //Set up their profile
        d1.setName("Daniel", "Tran");
        d2.setName("Sair", "Abbas");
        d3.setName("Josh", "Sjah");
        d4.setName("Alexis", "Arroyo");

        d1.setStatus("online");
        d2.setStatus("still here (dog)");
        d3.setStatus("playing ms");
        d4.setStatus("offline");

        //Add friends
        d1.addFriend(d2); //Daniel x Sair lmao
        d1.addFriend(d3); //Daniel x Josh
        d3.addFriend(d4); //Josh x Alexis
        d4.addFriend(d3); //Alexis x Josh

        //Add them to database
        model.addFriend(d1);
        model.addFriend(d2);
        model.addFriend(d3);
        model.addFriend(d4);

        //Add them to graph
        manager.addProfile(d1);
        manager.addProfile(d2);
        manager.addProfile(d3);
        manager.addProfile(d4);

        manager.createFriendship(d1, d2); //Daniel --- Sair
        manager.createFriendship(d1, d3); //Daniel --- Josh
        manager.createFriendship(d3, d4); //Josh --- Alexis
        manager.createFriendship(d4, d2); //Alexis --- Sair

    }

    public class HandleActionEventsForJButton extends JFrame implements ActionListener{

        HandleActionEventsForJButton() {
            //Set flow layout for the frame
            this.getContentPane().setLayout(new BorderLayout(10, 5)); //hGap, vGap;

            //Build the labels
            JLabel firstName = new JLabel("First Name: " + model.name);
            JLabel lastName = new JLabel("Last Name: ");
            JLabel status = new JLabel("Status: " + model.status);

            //Build the profile pic
            ImageIcon img = new ImageIcon(model.getProfilePicture());
            Image scaleImg = img.getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT);
            ImageIcon newImg = new ImageIcon(scaleImg);
            JLabel image = new JLabel(newImg);

            //Build the buttons
            JButton createButton = new JButton("Create Profile");
            JButton editButton = new JButton("Edit Profile");
            JButton addButton = new JButton("Add Friend");
            JButton joinButton = new JButton("Join Network");
            JButton leaveButton = new JButton("Leave Network");

            //Align the buttons vertically
            Box bv = Box.createVerticalBox();
            bv.add(createButton);
            bv.add(editButton);
            bv.add(addButton);
            bv.add(joinButton);
            bv.add(leaveButton);
            bv.add(image);
            bv.add(firstName);
            bv.add(lastName);
            bv.add(status);

            //Set action listeners for buttons
            createButton.addActionListener(this);
            editButton.addActionListener(this);
            addButton.addActionListener(this);
            joinButton.addActionListener(this);
            leaveButton.addActionListener(this);

            //Add buttons to the frame
            add(bv, BorderLayout.WEST); //Buttons stored in bv align down and to the left
        }

        /**
         * When a button pressed, calls a method/class object to run a new window or page.
         * @param ae the act of clicking button
         * */
        @Override
        public void actionPerformed(ActionEvent ae) {
            String action = ae.getActionCommand();

            //Opens a new Frame to enter user info
            if (action.equals("Create Profile")) {
                new ProfileForm(model);
            }
            else if (action.equals("Edit Profile")) {
                System.out.println("Edit Button pressed!");
            }
            else if (action.equals("Add Friend")) {
                System.out.println("Add Button pressed!");
            }
            else if (action.equals("Join Network")) {
                LoginView();
            }
            else if (action.equals("Leave Network")) {
                System.out.println("Leave Button pressed!");
            }
        }
    }

}

