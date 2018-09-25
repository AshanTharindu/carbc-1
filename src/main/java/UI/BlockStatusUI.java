package UI;

import core.blockchain.Block;
import core.consensus.ConsensusOld;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;

public class BlockStatusUI {

    private JPanel proposalPane;
    private JButton sendProposalButton;
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
    private JButton agreed;
    private JButton NOButton;
    private JTextArea blockStatus;
    private JTextArea blockStatusText;


    public BlockStatusUI(Block block) {
        vehicleIDText.setText(block.getTransaction().getTransactionInfo().getVehicleId());
        eventText.setText(block.getTransaction().getTransactionInfo().getEvent());
        para1Text.setText(String.valueOf(block.getTransaction().getTransactionInfo().getParameters()[0]));
        para2Text.setText(String.valueOf(block.getTransaction().getTransactionInfo().getParameters()[1]));
        senderPubKeyText.setText(block.getTransaction().getSender());
        roleText.setText(block.getTransaction().getValidations().get(0).getValidator().getRole());
        agreed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    System.out.println(block.getTransaction().getTransactionInfo().getSmartContractName());
                    ConsensusOld.getInstance().insertBlock(block);
                } catch (ParseException e1) {
                    e1.printStackTrace();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public void runBlockStatusUI(String status) {
        JFrame frame = new JFrame("BlockStatusUI");
        frame.setContentPane(this.proposalPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
//        JFrame frame = new JFrame("BlockStatusUI");
//        frame.setContentPane(new BlockStatusUI().proposalPane);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.pack();
//        frame.setVisible(true);
    }
}
