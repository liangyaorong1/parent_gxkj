package cn.gxkj.audit;

import cn.gxkj.audit.dao.ProcUserGroupDao;
import cn.gxkj.audit.entity.ProcUserGroup;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @description
 * @author:Liang
 * @CREATE:2022--07--31 13:36:00
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ProcUserGroupTest {
    @Autowired
    private ProcUserGroupDao procUserGroupDao;

    @Autowired
    private RepositoryService repositoryService;

    @Test
    public void test(){
        List<ProcUserGroup> all = procUserGroupDao.findAll();
        System.out.println(all.size());
    }

    @Test
    public void test2(){
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
        System.out.println(list.size());
    }
}
