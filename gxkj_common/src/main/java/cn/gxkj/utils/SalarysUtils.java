package cn.gxkj.utils;

import java.math.BigDecimal;

public class SalarysUtils {

	public static BigDecimal getTax(BigDecimal salary) {
		//salary * 阶梯税率 - 速算扣除数
		BigDecimal easyNum = new BigDecimal(0);
		BigDecimal stageNum = new BigDecimal(0.03);
		if (salary.doubleValue() > 1500) {
			easyNum = new BigDecimal(105);
			stageNum = new BigDecimal(0.1);
		} else if (salary.doubleValue() > 4500) {
			easyNum = new BigDecimal(555);
			stageNum = new BigDecimal(0.2);
		} else if (salary.doubleValue() > 9000) {
			easyNum = new BigDecimal(1005);
			stageNum = new BigDecimal(0.25);
		} else if (salary.doubleValue() > 35000) {
			easyNum = new BigDecimal(2755);
			stageNum = new BigDecimal(0.3);
		} else if (salary.doubleValue() > 55000) {
			easyNum = new BigDecimal(5505);
			stageNum = new BigDecimal(0.35);
		} else if (salary.doubleValue() > 55000) {
			easyNum = new BigDecimal(5505);
			stageNum = new BigDecimal(0.35);
		} else if (salary.doubleValue() > 80000) {
			easyNum = new BigDecimal(13505);
			stageNum = new BigDecimal(0.45);
		}
		salary = salary.multiply(stageNum);
		salary = salary.subtract(easyNum);
		return salary;
	}

}
