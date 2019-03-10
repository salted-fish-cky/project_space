package com.cky.upload.db;

import com.cky.upload.bean.FileUploadBean;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class FileUploadDaoImpTest {
    private FileUploadDao dao = new FileUploadDaoImp();
    @Test
    public void save() throws Exception {
    }



    @org.junit.Test
    public void get() throws Exception {
    }

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(FileUploadDaoImp.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

}
