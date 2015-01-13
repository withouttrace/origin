package org.blue.sys.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.blue.sys.dao.DeleteDao;
import org.blue.sys.vo.Author;
import org.blue.sys.vo.Editor;
import org.blue.sys.vo.Expert;

public class ImplDeleteDao implements DeleteDao {

    public boolean deleteAuthor(Connection conn, Author author)
            throws SQLException {
        String sql = "DELETE FROM TB_Author " + " WHERE author_id = ?";

        PreparedStatement pstm = null;

        try{
            pstm = conn.prepareStatement(sql);
            author.setIdStatementValue(pstm);
            pstm.executeUpdate();
        }
        finally{
            pstm.close();
            conn.close();
        }

        return true;

    }

    public boolean deleteExpert(Connection conn, Expert expert)
            throws SQLException {
        String sql = "DELETE FROM TB_Expert " + " WHERE expert_id = ?";

        PreparedStatement pstm = null;

        try{
            pstm = conn.prepareStatement(sql);
            expert.setIdStatementValue(pstm);
            pstm.executeUpdate();
        }
        finally{
            pstm.close();
            conn.close();
        }

        return true;
    }

    public boolean deleteEditor(Connection conn, Editor editor)
            throws SQLException {
        String sql = "DELETE FROM TB_Editor " + " WHERE editor_id = ?";

        PreparedStatement pstm = null;

        try{
            pstm = conn.prepareStatement(sql);
            editor.setIdStatementValue(pstm);
            pstm.executeUpdate();
        }
        finally{
            pstm.close();
            conn.close();
        }

        return true;
    }

    public boolean deleteEssay(Connection conn, int id) throws SQLException {
        String sql = "DELETE FROM TB_Essay " + " WHERE essay_id = '" + id + "'";

        PreparedStatement pstm = null;

        try{
            pstm = conn.prepareStatement(sql);
            pstm.executeUpdate();
        }
        finally{
            pstm.close();
            conn.close();
        }

        return true;
    }

}
