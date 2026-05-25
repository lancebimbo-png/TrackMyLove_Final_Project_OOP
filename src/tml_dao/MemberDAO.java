package tml_dao;

import tml_db.DatabaseConnection;
import tml_model.Member;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {

    public boolean insert(Member member) {
        String sql = "INSERT INTO members (household_id, full_name, role, location, password_hash) VALUES (?, ?, ?, ?, SHA2(?, 256))";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, member.getHouseholdId());
            ps.setString(2, member.getFullName());
            ps.setString(3, member.getRole());
            ps.setString(4, member.getLocation());
            ps.setString(5, member.getPasswordHash());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Insert member failed: " + e.getMessage());
            return false;
        }
    }

    public List<Member> findAll(int householdId) {
        List<Member> list = new ArrayList<>();
        String sql = "SELECT * FROM members WHERE household_id = ? ORDER BY full_name";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, householdId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.out.println("FindAll members failed: " + e.getMessage());
        }
        return list;
    }

    public Member findById(int id) {
        String sql = "SELECT * FROM members WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            System.out.println("FindById member failed: " + e.getMessage());
        }
        return null;
    }

    public Member findByNameAndPassword(String fullName, String password, int householdId) {
        String sql = "SELECT * FROM members WHERE full_name = ? AND password_hash = SHA2(?, 256) AND household_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fullName);
            ps.setString(2, password);
            ps.setInt(3, householdId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            System.out.println("Login query failed: " + e.getMessage());
        }
        return null;
    }

    public boolean update(Member member) {
        String sql = "UPDATE members SET full_name = ?, role = ?, location = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, member.getFullName());
            ps.setString(2, member.getRole());
            ps.setString(3, member.getLocation());
            ps.setInt(4, member.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Update member failed: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM members WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Delete member failed: " + e.getMessage());
            return false;
        }
    }

    public List<Member> search(String keyword, int householdId) {
        List<Member> list = new ArrayList<>();
        String sql = "SELECT * FROM members WHERE household_id = ? AND (full_name LIKE ? OR role LIKE ? OR location LIKE ?) ORDER BY full_name";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            ps.setInt(1, householdId);
            ps.setString(2, kw);
            ps.setString(3, kw);
            ps.setString(4, kw);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.out.println("Search members failed: " + e.getMessage());
        }
        return list;
    }

    public List<Member> findPatients(int householdId) {
        List<Member> list = new ArrayList<>();
        String sql = "SELECT * FROM members WHERE household_id = ? AND role = 'patient' ORDER BY full_name";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, householdId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.out.println("FindPatients failed: " + e.getMessage());
        }
        return list;
    }

    public List<Member> findCaregivers(int householdId) {
        List<Member> list = new ArrayList<>();
        String sql = "SELECT * FROM members WHERE household_id = ? AND role IN ('caregiver', 'viewer') ORDER BY full_name";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, householdId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.out.println("FindCaregivers failed: " + e.getMessage());
        }
        return list;
    }

    private Member mapRow(ResultSet rs) throws SQLException {
        Member m = new Member();
        m.setId(rs.getInt("id"));
        m.setHouseholdId(rs.getInt("household_id"));
        m.setFullName(rs.getString("full_name"));
        m.setRole(rs.getString("role"));
        m.setLocation(rs.getString("location"));
        m.setPasswordHash(rs.getString("password_hash"));
        m.setCreatedAt(rs.getString("created_at"));
        return m;
    }
}