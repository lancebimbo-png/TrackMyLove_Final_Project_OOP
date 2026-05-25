package tml_dao;

import tml_db.DatabaseConnection;
import tml_model.Medication;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicationDAO {

    public boolean insert(Medication medication) {
        String sql = "INSERT INTO medications (name, dosage, unit, purpose, notes) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, medication.getName());
            ps.setDouble(2, medication.getDosage());
            ps.setString(3, medication.getUnit());
            ps.setString(4, medication.getPurpose());
            ps.setString(5, medication.getNotes());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Insert medication failed: " + e.getMessage());
            return false;
        }
    }

    public List<Medication> findAll() {
        List<Medication> list = new ArrayList<>();
        String sql = "SELECT * FROM medications ORDER BY name";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.out.println("FindAll medications failed: " + e.getMessage());
        }
        return list;
    }

    public Medication findById(int id) {
        String sql = "SELECT * FROM medications WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            System.out.println("FindById medication failed: " + e.getMessage());
        }
        return null;
    }

    public boolean update(Medication medication) {
        String sql = "UPDATE medications SET name = ?, dosage = ?, unit = ?, purpose = ?, notes = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, medication.getName());
            ps.setDouble(2, medication.getDosage());
            ps.setString(3, medication.getUnit());
            ps.setString(4, medication.getPurpose());
            ps.setString(5, medication.getNotes());
            ps.setInt(6, medication.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Update medication failed: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM medications WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Delete medication failed: " + e.getMessage());
            return false;
        }
    }

    public List<Medication> search(String keyword) {
        List<Medication> list = new ArrayList<>();
        String sql = "SELECT * FROM medications WHERE name LIKE ? OR purpose LIKE ? OR unit LIKE ? ORDER BY name";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);
            ps.setString(3, kw);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.out.println("Search medications failed: " + e.getMessage());
        }
        return list;
    }

    public boolean existsByName(String name) {
        String sql = "SELECT id FROM medications WHERE name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("ExistsByName failed: " + e.getMessage());
            return false;
        }
    }

    private Medication mapRow(ResultSet rs) throws SQLException {
        Medication m = new Medication();
        m.setId(rs.getInt("id"));
        m.setName(rs.getString("name"));
        m.setDosage(rs.getDouble("dosage"));
        m.setUnit(rs.getString("unit"));
        m.setPurpose(rs.getString("purpose"));
        m.setNotes(rs.getString("notes"));
        return m;
    }
}