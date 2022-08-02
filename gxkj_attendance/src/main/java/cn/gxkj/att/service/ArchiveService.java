package cn.gxkj.att.service;

import cn.gxkj.IdWorker;
import cn.gxkj.att.dao.ArchiveMonthlyDao;
import cn.gxkj.att.dao.ArchiveMonthlyInfoDao;
import cn.gxkj.att.dao.AttendanceDao;
import cn.gxkj.att.dao.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class ArchiveService {

	@Autowired
	private AttendanceDao attendanceDao;

	@Autowired
	private ArchiveMonthlyDao atteArchiveMonthlyDao;

	@Autowired
	private ArchiveMonthlyInfoDao archiveMonthlyInfoDao;


	@Autowired
	private UserDao userDao;

	@Autowired
	private IdWorker idWorkker;
}
