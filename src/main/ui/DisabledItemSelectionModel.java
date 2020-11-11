package ui;

import javax.swing.*;

// credit: https://stackoverflow.com/questions/17863780/make-jlist-values-unselectable
public class DisabledItemSelectionModel extends DefaultListSelectionModel {
    @Override
    public void setSelectionInterval(int i0, int i1) {
        super.setSelectionInterval(0, 0);
    }
}
