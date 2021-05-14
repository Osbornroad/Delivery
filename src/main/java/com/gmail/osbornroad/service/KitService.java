package com.gmail.osbornroad.service;

import com.gmail.osbornroad.model.FinishPart;
import com.gmail.osbornroad.model.Kit;
import com.gmail.osbornroad.repository.jpa.KitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class KitService {

    @Autowired
    KitRepository kitRepository;

    @Transactional(readOnly = true)
    public List<Kit> findAllKits() {
        List<Kit> kitList = new ArrayList<>();
        Iterable<Kit> iterable = kitRepository.findAll();
        iterable.forEach(kitList::add);
        for(Kit kit : kitList) {
            Set<FinishPart> finishParts = new TreeSet<>(Comparator.comparingInt(FinishPart::getSortNum));
            finishParts.addAll(kit.getFinishPartSet());
            kit.setFinishPartSet(finishParts);
        }
        return kitList;
    }

    @Transactional(readOnly = true)
    public Kit findKitById(Integer id) {
        Optional<Kit> optional = kitRepository.findById(id);
        if(optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    public Kit saveKit(Kit kit) {
        return kitRepository.save(kit);
    }

    public void deleteKit(Kit kit) {
        kitRepository.delete(kit);
    }

/*    public Kit findKitByWib224(String wib224) {
        return kitRepository.findByWib224(wib224);
    }*/

    public List<String> getCurrentWib224List() {
        List<Kit> currentKits = kitRepository.findByCurrentIsTrue();
        List<String> currentWib224 = new ArrayList<>();
        for(Kit kit : currentKits) {
            if(kit.isCurrent())
                currentWib224.add(kit.getWib224());
        }
        return currentWib224;
    }

    public List<String> getCurrentKitNameList() {
        List<Kit> currentKits = kitRepository.findByCurrentIsTrue();
        List<String> currentKitNames = new ArrayList<>();
        for(Kit kit : currentKits) {
            currentKitNames.add(kit.getKitName());
        }
        return currentKitNames;
    }
}
