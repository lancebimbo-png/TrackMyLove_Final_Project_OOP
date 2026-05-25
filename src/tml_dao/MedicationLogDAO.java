package tml_dao;

import tml_db.DatabaseConnection;
import tml_model.MedicationLog;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicationLogDAO {

    public int insert(MedicationLog log) {
        String sql = "INSERT INTO medication_logs (patient_id, given_by_id, log_date, notes) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, log.getPatientId());
            ps.setInt(2, log.getGivenById());
            ps.setString(3, log.getLogDate());
            ps.setString(4, log.getNotes());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Insert log failed: " + e.getMessage());
        }
        return -1;
    }

    public List<MedicationLog> findAll(int householdId) {
        List<MedicationLog> list = new ArrayList<>();
        String sql = "SELECT ml.*, p.full_name AS patient_name, g.full_name AS given_by_name " +
                     "FROM medication_logs ml " +
                     "JOIN members p ON ml.patient_id  = p.id " +
                     "JOIN members g ON ml.given_by_id = g.id " +
                     "WHERE p.household_id = ? " +
                     "ORDER BY ml.log_date DESC, ml.logged_at DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, householdId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.out.println("FindAll logs failed: " + e.getMessage());
        }
        return list;
    }

    public MedicationLog findById(int id) {
        String sql = "SELECT ml.*, p.full_name AS patient_name, g.full_name AS given_by_name " +
                     "FROM medication_logs ml " +
                     "JOIN members p ON ml.patient_id  = p.id " +
                     "JOIN members g ON ml.given_by_id = g.id " +
                     "WHERE ml.id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            System.out.println("FindById log failed: " + e.getMessage());
        }
        return null;
    }

    public List<MedicationLog> findByPatient(int patientId) {
        List<MedicationLog> list = new ArrayList<>();
        String sql = "SELECT ml.*, p.full_name AS patient_name, g.full_name AS given_by_name " +
                     "FROM medication_logs ml " +
                     "JOIN members p ON ml.patient_id  = p.id " +
                     "JOIN members g ON ml.given_by_id = g.id " +
                     "WHERE ml.patient_id = ? " +
                     "ORDER BY ml.log_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.out.println("FindByPatient failed: " + e.getMessage());
        }
        return list;
    }

    public List<MedicationLog> findByDate(String date, int householdId) {
        List<MedicationLog> list = new ArrayList<>();
        String sql = "SELECT ml.*, p.full_name AS patient_name, g.full_name AS given_by_name " +
                     "FROM medication_logs ml " +
                     "JOIN members p ON ml.patient_id  = p.id " +
                     "JOIN members g ON ml.given_by_id = g.id " +
                     "WHERE ml.log_date = ? AND p.household_id = ? " +
                     "ORDER BY ml.logged_at DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, date);
            ps.setInt(2, householdId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.out.println("FindByDate failed: " + e.getMessage());
        }
        return list;
    }

    public List<MedicationLog> search(String keyword, int householdId) {
        List<MedicationLog> list = new ArrayList<>();
        String sql = "SELECT ml.*, p.full_name AS patient_name, g.full_name AS given_by_name " +
                     "FROM medication_logs ml " +
                     "JOIN members p ON ml.patient_id  = p.id " +
                     "JOIN members g ON ml.given_by_id = g.id " +
                     "WHERE p.household_id = ? " +
                     "AND (p.full_name LIKE ? OR g.full_name LIKE ? OR ml.log_date LIKE ? OR ml.notes LIKE ?) " +
                     "ORDER BY ml.log_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            ps.setInt(1, householdId);
            ps.setString(2, kw);
            ps.setString(3, kw);
            ps.setString(4, kw);
            ps.setString(5, kw);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.out.println("Search logs failed: " + e.getMessage());
        }
        return list;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM medication_logs WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Delete log failed: " + e.getMessage());
            return false;
        }
    }

    private MedicationLog mapRow(ResultSet rs) throws SQLException {
        MedicationLog log = new MedicationLog();
        log.setId(rs.getInt("id"));
        log.setPatientId(rs.getInt("patient_id"));
        log.setGivenById(rs.getInt("given_by_id"));
        log.setLogDate(rs.getString("log_date"));
        log.setNotes(rs.getString("notes"));
        log.setLoggedAt(rs.getString("logged_at"));
        log.setPatientName(rs.getString("patient_name"));
        log.setGivenByName(rs.getString("given_by_name"));
        return log;
    }
}