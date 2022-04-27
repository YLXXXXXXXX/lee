package priv.jesse.ercode.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import priv.jesse.ercode.entity.Information;

import java.util.List;

public interface InformationDao extends JpaRepository<Information, Integer> {
    List<Information> findAllByUserId(int userId);

    List<Information> findAllByUserIdAndProductId(int userId,int productId);

    List<Information> findAllByProductId(int productId);
}


