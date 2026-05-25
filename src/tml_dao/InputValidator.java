package tml_dao;

import javax.swing.JOptionPane;
import java.util.regex.Pattern;

public class InputValidator {

    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static boolean isValidDate(String date) {
        if (!isNotEmpty(date)) return false;
        return Pattern.matches("\\d{4}-\\d{2}-\\d{2}", date.trim());
    }

    public static boolean isValidTime(String time) {
        if (!isNotEmpty(time)) return false;
        return Pattern.matches("([01]\\d|2[0-3]):[0-5]\\d(:[0-5]\\d)?", time.trim());
    }

    public static boolean isValidDosage(String dosage) {
        if (!isNotEmpty(dosage)) return false;
        try {
            double val = Double.parseDouble(dosage.trim());
            return val > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidPassword(String password) {
        return isNotEmpty(password) && password.trim().length() >= 4;
    }

    public static boolean hasLettersOnly(String value) {
        if (!isNotEmpty(value)) return false;
        return Pattern.matches("[a-zA-Z\\s.'-]+", value.trim());
    }

    public static void showError(java.awt.Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Invalid Input", JOptionPane.ERROR_MESSAGE);
    }

    public static void showSuccess(java.awt.Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean confirmDelete(java.awt.Component parent, String itemName) {
        int result = JOptionPane.showConfirmDialog(
            parent,
            "Are you sure you want to delete " + itemName + "?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        return result == JOptionPane.YES_OPTION;
    }

    public static boolean confirmAction(java.awt.Component parent, String message) {
        int result = JOptionPane.showConfirmDialog(
            parent,
            message,
            "Confirm",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        return result == JOptionPane.YES_OPTION;
    }
}