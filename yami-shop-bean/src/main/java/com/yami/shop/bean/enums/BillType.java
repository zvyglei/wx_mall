/*

 */

package com.yami.shop.bean.enums;

/**
 * @author lanhai
 */
public enum BillType {

	/** 收入 */
	IN(0,"收入"),

	/** "支付宝 */
	OUT(1,"支出");

	private Integer type;

	private String typeName;

	public Integer value() {
		return type;
	}

	public String getPayTypeName() {
		return typeName;
	}

	BillType(Integer num, String payTypeName){
		this.type = num;
		this.typeName = payTypeName;
	}

	public static BillType instance(Integer value) {
		BillType[] enums = values();
		for (BillType statusEnum : enums) {
			if (statusEnum.value().equals(value)) {
				return statusEnum;
			}
		}
		return null;
	}
}
