package UI;

import core.blockchain.Block;

import javax.swing.*;

public class BlockStatusUI2 {
    private JPanel proposalPane;
    private JLabel blockDetails;
    private JPanel mainPanel;
    private JPanel Panel;
    private JLabel eventButton;
    private JTextField eventText;
    private JLabel vehicleID;
    private JTextField vehicleIDText;
    private JLabel para1;
    private JTextField para1Text;
    private JLabel para2Label;
    private JTextField para2Text;
    private JLabel senderPublicKey;
    private JTextField senderPubKeyText;
    private JLabel isMandotory;
    private JCheckBox isMandatoryCheckBox;
    private JLabel role;
    private JTextField roleText;
    private JLabel priority;
    private JTextField priorityText;

    public BlockStatusUI2(Block block) {
//        vehicleIDText.setText(block.getTransaction().getTransactionInfo().getVehicleId());
//        eventText.setText(block.getTransaction().getTransactionInfo().getEvent());
//        para1Text.setText(String.valueOf(block.getTransaction().getTransactionInfo().getParameters()[0]));
//        para2Text.setText(String.valueOf(block.getTransaction().getTransactionInfo().getParameters()[1]));
//        senderPubKeyText.setText(block.getTransaction().getSender());
//        roleText.setText(block.getTransaction().getValidations().get(0).getValidator().getRole());
    }

    public void runBlockStatusUI2() {
        JFrame frame = new JFrame("BlockStatusUI");
        frame.setContentPane(this.proposalPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
