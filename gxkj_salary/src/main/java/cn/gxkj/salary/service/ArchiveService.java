package cn.gxkj.salary.service;


import cn.gxkj.IdWorker;
import cn.gxkj.salary.dao.ArchiveDao;
import cn.gxkj.salary.dao.ArchiveDetailDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ArchiveService {
    @Autowired
    private ArchiveDao archiveDao;
    @Autowired
    private ArchiveDetailDao archiveDetailDao;
    @Autowired
    private IdWorker idWorker;

}
