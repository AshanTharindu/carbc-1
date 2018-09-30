package UI;

import chainUtil.ChainUtil;
import chainUtil.KeyGenerator;
import core.blockchain.Block;
import network.communicationHandler.MessageSender;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

public class AgreementRequest {
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
    private JPanel mainPanel;


    public AgreementRequest(Block block) {
//        vehicleIDText.setText(block.getTransaction().getTransactionInfo().getVehicleId());
//        eventText.setText(block.getTransaction().getTransactionInfo().getEvent());
//        para1Text.setText(String.valueOf(block.getTransaction().getTransactionInfo().getParameters()[0]));
//        para2Text.setText(String.valueOf(block.getTransaction().getTransactionInfo().getParameters()[1]));
//        senderPubKeyText.setText(block.getTransaction().getSender());
//        roleText.setText(block.getTransaction().getValidations().get(0).getValidator().getRole());
        agreed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    byte[] signature = ChainUtil.sign(KeyGenerator.getInstance().getPrivateKey(),"agreed");
                    MessageSender.getInstance().sendAgreement(block,1,"agreed",signature);
                } catch (NoSuchProviderException e1) {
                    e1.printStackTrace();
                } catch (NoSuchAlgorithmException e1) {
                    e1.printStackTrace();
                } catch (SignatureException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (InvalidKeySpecException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public void runAgreementRequestUI() {
        JFrame frame = new JFrame("Agreement Request");
        frame.setContentPane(this.mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
