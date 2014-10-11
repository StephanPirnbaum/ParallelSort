package main.java.gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import main.java.AlgorithmExecutor;
import org.w3c.dom.ranges.RangeException;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by stephan on 6/20/14.
 */
public class SortGui {
    private JCheckBox quickListSingleCheckBox;
    private JCheckBox quickArraySingleCheckBox;
    private JCheckBox quickListMultiCheckBox;
    private JCheckBox quickArrayMultiCheckBox;
    private JCheckBox quickListForkCheckBox;
    private JCheckBox quickArrayForkCheckBox;
    private JCheckBox mergeListSingleCheckBox;
    private JCheckBox mergeListMultiCheckBox;
    private JCheckBox mergeListForkCheckBox;
    private JCheckBox mergeArraySingleCheckBox;
    private JCheckBox mergeArrayMultiCheckBox;
    private JCheckBox mergeArrayForkCheckBox;
    private JTextField numberOfThreadsField;
    private JTextField lowerExponentField;
    private JTextField upperExponentField;
    private JTextArea resultPane;
    private JButton runButton;
    private JButton resetButton;
    private JPanel mainPanel;
    private JTextField numberOfRunsField;
    private JTextField minimalSizeField;
    private JProgressBar progressBar1;
    private JCheckBox javaArraySingleCheckBox;
    private JCheckBox javaArrayMultiCheckBox;
    private Map<String, JCheckBox> jCheckBoxes;
    private int lowerExponent, upperExponent, numberOfRuns, numberOfThreads, minimalSizeExponent;

    public SortGui() {
        jCheckBoxes = new TreeMap<>();
        jCheckBoxes.put("javaArraySingle", javaArraySingleCheckBox);
        jCheckBoxes.put("javaArrayMulti", javaArrayMultiCheckBox);
        jCheckBoxes.put("quickArraySingle", quickArraySingleCheckBox);
        jCheckBoxes.put("quickArrayMulti", quickArrayMultiCheckBox);
        jCheckBoxes.put("quickArrayFork", quickArrayForkCheckBox);
        jCheckBoxes.put("quickListSingle", quickListSingleCheckBox);
        jCheckBoxes.put("quickListMulti", quickListMultiCheckBox);
        jCheckBoxes.put("quickListFork", quickListForkCheckBox);
        jCheckBoxes.put("mergeArraySingle", mergeArraySingleCheckBox);
        jCheckBoxes.put("mergeArrayMulti", mergeArrayMultiCheckBox);
        jCheckBoxes.put("mergeArrayFork", mergeArrayForkCheckBox);
        jCheckBoxes.put("mergeListSingle", mergeListSingleCheckBox);
        jCheckBoxes.put("mergeListMulti", mergeListMultiCheckBox);
        jCheckBoxes.put("mergeListFork", mergeListForkCheckBox);
        initializeResetButton();
        initializeRunButton();

    }

    private void initializeResetButton() {
        resetButton.addActionListener(e -> {
            jCheckBoxes.forEach((k, v) -> v.setSelected(false));
            resultPane.setText("");
            lowerExponentField.setText("");
            upperExponentField.setText("");
            numberOfThreadsField.setText("");
            numberOfRunsField.setText("");
            minimalSizeField.setText("");
        });
    }

    private void initializeRunButton() {
        runButton.addActionListener(e -> {
            if (!readInputFields())
                return;
            resultPane.setText("");
            List<String> selectedBoxes = new LinkedList<>();
            jCheckBoxes.forEach((k, v) -> {
                if (v.isSelected()) selectedBoxes.add(k);
            });
            progressBar1.setMinimum(0);
            progressBar1.setMaximum((upperExponent - lowerExponent + 1) * numberOfRuns * selectedBoxes.size());
            progressBar1.setValue(0);
            AlgorithmExecutor algorithmExecutor = new AlgorithmExecutor(selectedBoxes, lowerExponent,
                    upperExponent, numberOfThreads, minimalSizeExponent, numberOfRuns, this);
            algorithmExecutor.start();
        });
    }

    public void printSpeedup(Map<String, ArrayList<Double>> speedUpMap) {
//        speedUpMap.forEach((algorithm, speedupList) -> speedupList.forEach(speedUp -> resultPane.append(algorithm + ": " + speedUp + "\n")));
        speedUpMap.forEach((algorithm, speedupList) ->
                resultPane.append(algorithm + ": " +
                        speedupList.stream()
                                .mapToDouble(a -> a)
//                                .filter(a -> a > 1 && a < Runtime.getRuntime().availableProcessors())
                                .filter(a -> a < Runtime.getRuntime().availableProcessors())
                                .average()
                                .getAsDouble() + "\n"));
    }

    private boolean readInputFields() {
        try {
            lowerExponent = Integer.parseInt(lowerExponentField.getText());
            if (lowerExponent < 0)
                throw new RangeException((short) 1, "Lower exponent is less than 0");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(mainPanel, "Lower exponent is not an int", "Range Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (RangeException ex) {
            JOptionPane.showMessageDialog(mainPanel, ex.getMessage());
            return false;
        }
        try {
            upperExponent = Integer.parseInt(upperExponentField.getText());
            if (upperExponent < lowerExponent)
                throw new RangeException((short) 2, "Upper exponent is less than lower exponent");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(mainPanel, "Upper exponent is not an int", "Range Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (RangeException ex) {
            JOptionPane.showMessageDialog(mainPanel, ex.getMessage());
            return false;
        }
        try {
            numberOfRuns = Integer.parseInt(numberOfRunsField.getText());
            if (numberOfRuns < 1)
                throw new RangeException((short) 3, "Number of runs has to be at least 1");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(mainPanel, "Number of runs is not an int", "Range Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (RangeException ex) {
            JOptionPane.showMessageDialog(mainPanel, ex.getMessage());
            return false;
        }
        try {
            numberOfThreads = Integer.parseInt(numberOfThreadsField.getText());
            if (numberOfThreads < 1)
                throw new RangeException((short) 4, "Number of threads has to be at least 1");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(mainPanel, "Number of threads is not an int", "Range Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (RangeException ex) {
            JOptionPane.showMessageDialog(mainPanel, ex.getMessage());
            return false;
        }
        try {
            minimalSizeExponent = Integer.parseInt(minimalSizeField.getText());
            if (minimalSizeExponent < 1)
                throw new RangeException((short) 5, "Threshold exponent has to be at least 1");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(mainPanel, "Threshold is not an int", "Range Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (RangeException ex) {
            JOptionPane.showMessageDialog(mainPanel, ex.getMessage());
            return false;
        }
        return true;
    }

    public void incrementProgressBar() {
        progressBar1.setValue(progressBar1.getValue() + 1);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("SortGui");
        frame.setContentPane(new SortGui().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(18, 4, new Insets(0, 0, 0, 0), -1, -1));
        final JLabel label1 = new JLabel();
        label1.setText("Single Thread");
        mainPanel.add(label1, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Multi Thread");
        mainPanel.add(label2, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(118, 15), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("ForkJoin");
        mainPanel.add(label3, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Algorithm");
        mainPanel.add(label4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Versions to run:");
        mainPanel.add(label5, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("QuickSort");
        mainPanel.add(label6, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("MergeSort");
        mainPanel.add(label7, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        quickListSingleCheckBox = new JCheckBox();
        quickListSingleCheckBox.setText("ArrayList");
        mainPanel.add(quickListSingleCheckBox, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        quickArraySingleCheckBox = new JCheckBox();
        quickArraySingleCheckBox.setText("int[]");
        mainPanel.add(quickArraySingleCheckBox, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        quickListMultiCheckBox = new JCheckBox();
        quickListMultiCheckBox.setText("ArrayList");
        mainPanel.add(quickListMultiCheckBox, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(118, 24), null, 0, false));
        quickArrayMultiCheckBox = new JCheckBox();
        quickArrayMultiCheckBox.setText("int[]");
        mainPanel.add(quickArrayMultiCheckBox, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(118, 24), null, 0, false));
        quickListForkCheckBox = new JCheckBox();
        quickListForkCheckBox.setText("ArrayList");
        mainPanel.add(quickListForkCheckBox, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        quickArrayForkCheckBox = new JCheckBox();
        quickArrayForkCheckBox.setText("int[]");
        mainPanel.add(quickArrayForkCheckBox, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        mergeListSingleCheckBox = new JCheckBox();
        mergeListSingleCheckBox.setText("ArrayList");
        mainPanel.add(mergeListSingleCheckBox, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        mergeListMultiCheckBox = new JCheckBox();
        mergeListMultiCheckBox.setText("ArrayList");
        mainPanel.add(mergeListMultiCheckBox, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(118, 24), null, 0, false));
        mergeListForkCheckBox = new JCheckBox();
        mergeListForkCheckBox.setText("ArrayList");
        mainPanel.add(mergeListForkCheckBox, new GridConstraints(4, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        mergeArraySingleCheckBox = new JCheckBox();
        mergeArraySingleCheckBox.setText("int[]");
        mainPanel.add(mergeArraySingleCheckBox, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        mergeArrayMultiCheckBox = new JCheckBox();
        mergeArrayMultiCheckBox.setText("int[]");
        mainPanel.add(mergeArrayMultiCheckBox, new GridConstraints(5, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(118, 24), null, 0, false));
        mergeArrayForkCheckBox = new JCheckBox();
        mergeArrayForkCheckBox.setText("int[]");
        mainPanel.add(mergeArrayForkCheckBox, new GridConstraints(5, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("Settings:");
        mainPanel.add(label8, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("Threads to use:");
        mainPanel.add(label9, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label10 = new JLabel();
        label10.setText("Range of number of elements (power to 2):");
        mainPanel.add(label10, new GridConstraints(9, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        numberOfThreadsField = new JTextField();
        numberOfThreadsField.setText("");
        mainPanel.add(numberOfThreadsField, new GridConstraints(8, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        lowerExponentField = new JTextField();
        mainPanel.add(lowerExponentField, new GridConstraints(9, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        runButton = new JButton();
        runButton.setText("Run");
        mainPanel.add(runButton, new GridConstraints(12, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        resetButton = new JButton();
        resetButton.setText("Reset");
        mainPanel.add(resetButton, new GridConstraints(13, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label11 = new JLabel();
        label11.setText("Number of runs per size:");
        mainPanel.add(label11, new GridConstraints(11, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        numberOfRunsField = new JTextField();
        mainPanel.add(numberOfRunsField, new GridConstraints(11, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label12 = new JLabel();
        label12.setText("Threshold (power to 2):");
        mainPanel.add(label12, new GridConstraints(10, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        minimalSizeField = new JTextField();
        mainPanel.add(minimalSizeField, new GridConstraints(10, 2, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        upperExponentField = new JTextField();
        mainPanel.add(upperExponentField, new GridConstraints(9, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        progressBar1 = new JProgressBar();
        mainPanel.add(progressBar1, new GridConstraints(14, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label13 = new JLabel();
        label13.setText("Java Reference");
        mainPanel.add(label13, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        javaArraySingleCheckBox = new JCheckBox();
        javaArraySingleCheckBox.setText("int[]");
        mainPanel.add(javaArraySingleCheckBox, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        javaArrayMultiCheckBox = new JCheckBox();
        javaArrayMultiCheckBox.setText("int[]");
        mainPanel.add(javaArrayMultiCheckBox, new GridConstraints(6, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        mainPanel.add(scrollPane1, new GridConstraints(12, 1, 6, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        resultPane = new JTextArea();
        scrollPane1.setViewportView(resultPane);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
