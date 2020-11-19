package com.dmsdbj.integral.backstage.provider.service;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.dmsdbj.integral.backstage.pojo.ProjectEntity;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Service
public class EncryptionService {
    public ProjectEntity encodeByMode(String englishName){
        ProjectEntity projectEntity = new ProjectEntity();
        StringBuffer buf = new StringBuffer("");
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(englishName.getBytes());
            byte b[] = md.digest();
            int i = 0;
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }

        } catch ( NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        projectEntity.setSecretId(IdWorker.getIdStr());
        projectEntity.setSecretKey(buf.toString());
        return projectEntity;
    }
}
