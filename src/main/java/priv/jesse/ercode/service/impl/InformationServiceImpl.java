package priv.jesse.ercode.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import priv.jesse.ercode.dao.InformationDao;
import priv.jesse.ercode.entity.Information;
import priv.jesse.ercode.service.InformationService;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class InformationServiceImpl implements InformationService {

    @Autowired
    private InformationDao informationDao;


    @Override
    public Information findById(int id) {
        return informationDao.findOne(id);
    }

    @Override
    public Page<Information> findAll(Pageable pageable) {
        return informationDao.findAll(pageable);
    }

    @Override
    public List<Information> findAllByProductId(int productId) {
        return informationDao.findAllByProductId(productId);
    }


    @Override
    public List<Information> findAllByUserIdAndProductId(int userId, int productId) {
        return informationDao.findAllByUserIdAndProductId(userId, productId);
    }

    @Override
    public void update(Information information) {
        informationDao.save(information);
    }

    @Override
    public int create(Information information) {
        return informationDao.save(information).getId();
    }

    @Override
    public void delById(int id) {
        informationDao.delete(id);
    }

    @Override
    public Map<String, List<Information>> getInfoDetail(int productId) {
        List<Information> list = findAllByProductId(productId);
        Map<String, List<Information>> listMap = new LinkedHashMap<>();
        List<Information> adminList = new ArrayList<>();
        List<Information> factoryList = new ArrayList<>();
        List<Information> checkList = new ArrayList<>();
        List<Information> shopList = new ArrayList<>();
        for (Information information : list) {
            if (information.getLevel() == 1) {
                adminList.add(information);
            } else if (information.getLevel() == 2) {
                factoryList.add(information);
            } else if (information.getLevel() == 3) {
                checkList.add(information);
            } else if (information.getLevel() == 4) {
                shopList.add(information);
            }
        }
        listMap.put("管理员", adminList);
        listMap.put("工厂", factoryList);
        listMap.put("质检员", checkList);
        listMap.put("商家", shopList);
        return listMap;
    }
}
