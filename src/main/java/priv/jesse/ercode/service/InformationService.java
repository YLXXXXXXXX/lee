package priv.jesse.ercode.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import priv.jesse.ercode.entity.Information;

import java.util.List;
import java.util.Map;

public interface InformationService {

    Information findById(int id);

    Page<Information> findAll(Pageable pageable);

    List<Information> findAllByProductId(int productId);

    List<Information> findAllByUserIdAndProductId(int userId, int productId);

    void update(Information information);


    int create(Information information);

    void  delById(int id);

    Map<String, List<Information>> getInfoDetail(int productId);

}
