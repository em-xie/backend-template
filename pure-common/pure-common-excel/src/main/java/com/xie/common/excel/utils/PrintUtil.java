package com.xie.common.excel.utils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PrintUtil {
	@Data
	public static class PrintOrder{
		private String orderNo;
		@Setter(AccessLevel.PRIVATE)
		private String orderNum;
		private String storeName;
		private String seatName;
		private LocalDateTime paymenyTime;
		private String remark;
		private Integer tablewareNumber;
		
		@Setter(AccessLevel.PRIVATE)
		private String orderSimple;
		@Setter(AccessLevel.PRIVATE)
		private String orderDetail;
		@Setter(AccessLevel.PRIVATE)
		private String orderAll;
		@Setter(AccessLevel.PRIVATE)
		private BigDecimal totalAmount;
		private BigDecimal actualAmout;
		
		private List<Order> orders;
		public String getOrderNum(){
			if(Objects.nonNull(orderNum)) {
				return orderNum;
			}
			orderNum = "9999";
			return orderNum;
		}
		public BigDecimal getTotalAmount(){
			if(Objects.nonNull(totalAmount)) {
				return totalAmount;
			}
			if (Objects.isNull(orders) || orders.isEmpty()) {
				return BigDecimal.ZERO;
			}
			totalAmount = orders.stream().reduce(BigDecimal.ZERO, (x, y) -> x.add(y.getPrice().multiply(BigDecimal.valueOf(y.getAmount()))), BigDecimal::add);
			return totalAmount;
		}
	    public String getPaymenyTime() {
			if (Objects.isNull(paymenyTime)) {
				return null;
			}
			return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(paymenyTime);
		}
	    public String getOrderSimple() {
	    	if(StringUtils.isNotBlank(orderSimple)) {
				return orderSimple;
			}
	    	if (Objects.isNull(orders) || orders.isEmpty()) {
				return null;
			}
	    	orderSimple = getOrder(orders, 26, 0, 4, 0);
	    	return orderSimple;
		}
	    public String getOrderDetail() {
	    	if(StringUtils.isNotBlank(orderDetail)) {
				return orderDetail;
			}
	    	if (Objects.isNull(orders) || orders.isEmpty()) {
				return null;
			}
	    	orderDetail = getOrder(orders, 18, 0, 4, 7);
	    	return orderDetail;
		}
	    public String getOrderAll() {
	    	if(StringUtils.isNotBlank(orderAll)) {
				return orderAll;
			}
	    	if (Objects.isNull(orders) || orders.isEmpty()) {
				return null;
			}
	    	orderAll = getOrder(orders, 14, 6, 3, 6);
	    	return orderAll;
		}
	}
	
	@Data
	public static class Order {
		/**
		 * 商品名称
		 */
		private String name;
		
		/**
		 * 商品单价
		 */
		private BigDecimal price;
		
		/**
		 * 商品数量
		 */
		private Integer amount;
	}
    /**
     * 生成订单字符串
     * @param orders 订单列表
     * @param nameLength 名称长度
     * @param priceLength 价格长度
     * @param numLength 数量长度
     * @param totalLength 总价长度
     * @return
     */
	public static String getOrder(List<Order> orders, int nameLength, int priceLength, int numLength, int totalLength) {
    	String orderInfo = "";
		for (Order order : orders) {
			String title = order.getName(); // 名称
			BigDecimal price = order.getPrice(); // 单价
			Integer num = order.getAmount(); // 数量
    		BigDecimal total = price.multiply(BigDecimal.valueOf(num)); // 总价
    		String priceText = addSpace(price.toPlainString(), priceLength); // 单价文本
    		String numText = addSpace("*" + String.valueOf(num), numLength); // 数量文本
    		String totalText = addSpace(total.toPlainString(), totalLength); // 总价文本
    		// 其他信息
    		String otherStr =  " ";
    		// 添加单价文本
    		if(priceLength != 0) {
    			otherStr += priceText;
    		}
    		// 添加数量文本
    		otherStr += numText;
    		// 添加总价文本
    		if(totalLength != 0) {
    			otherStr += " " + totalText;
    		}
    		Integer tl = getLength(title); // 获取字符串长度
    		if (tl < nameLength) {
    			// 如果标题长度小于设置长度
    			// 添加空格
    			// 添加其他信息
				title = title + addSpace("", nameLength - tl) + otherStr;
			} else if (tl == nameLength) {
				// 如果标题长度等于设置长度
				// 直接添加其他信息
				title += otherStr;
			} else {
				// 单行标题长度
				Integer length = 0;
				// 单行标题内容
				StringBuffer sb = new StringBuffer();
				// 存放多行标题内容
				List<String> list = new ArrayList<String>();
				for (char str : title.toCharArray()) {
					// 叠加标题长度
					length += getCharLength(str);
					if(length <= nameLength) {
						// 如果标题长度小于设置长度 直接将当前字符添加到标题内容中
						sb.append(str);
					} else {
						// 将之前的标题内容添加到多行标题内容中
						list.add(sb.toString());
						// 重新计算当前字符的标题长度
						length = getCharLength(str);
						// 清空标题内容
						sb.setLength(0);
						// 重新将当前字符添加到标题内容中
						sb.append(str);
					}
				}
				// 跳出循环的剩余标题内容添加到多行标题内容中
				list.add(sb.toString());
				// 清空标题从多行标题内容中获取
				title = "";
				for (int i = 0; i < list.size(); i++) {
					// 获取当前行标题内容
					String str = list.get(i);
					if (i == 0) {
						// 第一行标题内容
						// 添加空格
						// 添加其他信息
						title += str + addSpace("", nameLength - getLength(str)) + otherStr + "<BR>\n";
					} else if(i != list.size() - 1){
						// 非首行和非尾行添加换行
						title += str + "<BR>\n";
					} else if(i == list.size() - 1) {
						// 尾行忽略换行
						title += str;
					}
				}
			}
    		// 订单信息列表
    		orderInfo += title + "<BR>\n";
		}
    	return orderInfo;
    }
    
    // 计算字符长度
    private static Integer getCharLength(char c) {
    	if (c <= 0xFF) {
    		return 1;
		}
    	return 2;
    }
    
    // 计算字符串长度
    private static Integer getLength(String str) {
    	Integer num = 0;
    	for (char c : str.toCharArray()) {
    		num += getCharLength(c);
    	}
    	return num;
    }
    
    // 添加空格
    private static String addSpace(String str, int size) {
		return StringUtils.rightPad(str, size, " ");
	}
	
	public static void main(String[] args) {
		PrintOrder p = new PrintOrder();
		p.setOrderNo("202012081");
		p.setPaymenyTime(LocalDateTime.now());
		p.setStoreName("国际旅行社");
		p.setSeatName("包厢A001号");
		p.setRemark("不要辣椒, 不要香菜, 多放点醋, 少加一点盐");
		p.setActualAmout(BigDecimal.valueOf(3000.80));
		p.setTablewareNumber(4);
		
		List<Order> orders = new ArrayList<Order>();
		Order order1 = new Order();
		order1.setName("酸菜鱼");
		order1.setPrice(BigDecimal.valueOf(69.42));
		order1.setAmount(100);
		Order order2 = new Order();
		order2.setName("可乐鸡翅加蒜蓉蒸扇贝");
		order2.setPrice(BigDecimal.valueOf(42.33));
		order2.setAmount(600);
		Order order3 = new Order();
		order3.setName("紫苏焖鹅加梅菜肉饼加椒盐虾加北京烤鸭");
		order3.setPrice(BigDecimal.valueOf(380.67));
		order3.setAmount(800);
		orders.add(order1);
		orders.add(order2);
		orders.add(order3);
		p.setOrders(orders);
		
		// 测试 SpringEL 解析器
		String template = "订单编号: #{#p.orderNo}<BR>\n";
		template += "--------------------------------<BR>\n";
		template += "<CB>##{#p.orderNum} 包厢A001</CB><BR>\n";
		template += "<C>#{#p.storeName}</C><BR>\n";
		template += "--------------------------------<BR>\n";
		template += "支付时间：#{#p.paymenyTime}<BR>\n";
		template += "人数：#{#p.tablewareNumber}<BR>\n";
		template += "--------------------------------<BR>\n";
		template += "<B>备注: #{#p.remark}</B><BR>\n";
		template += "----------  订单详情  ----------<BR>\n";
		template += "#{#p.orderDetail}\n";
		template += "--------------------------------<BR>\n";
		template += "<RIGHT>订单金额：#{#p.totalAmount} 元</RIGHT><BR>\n";
		template += "（详情查看手机端）<RIGHT><BOLD>#{#p.actualAmout} 元</BOLD></RIGHT><BR>\n";
		template += "--------------------------------<BR>\n";
		template += "经系统判定，点餐用户是【新顾客】<BR>";
		template += "<CB>##{#p.orderNum} 完</CB><BR>";
		System.out.println(template);
		ExpressionParser paser = new SpelExpressionParser();// 创建表达式解析器
		
		// 通过evaluationContext.setVariable可以在上下文中设定变量。
		EvaluationContext context = new StandardEvaluationContext();
		context.setVariable("p", p);
		
		// 解析表达式，如果表达式是一个模板表达式，需要为解析传入模板解析器上下文。
		Expression expression = paser.parseExpression(template, new TemplateParserContext());
		System.out.println(expression.getValue(context, String.class));
		
		// 使用Expression.getValue()获取表达式的值，这里传入了Evalution上下文，第二个参数是类型参数，表示返回值的类型。
		System.out.println(expression.getValue(context, String.class));
		
		// Result editPrinter = PrintUtil.printMsg("922623149", expression.getValue(context, String.class).replace("\n", ""), 1);
		// System.out.println(editPrinter);
	}
}