package design.pattern.strategy;

public class PrimaryMemberStrategy implements MemberStrategy {

	@Override
	public double calcPrice(double booksPrice) {
		System.out.println("对于初级会员的没有折扣");
		return booksPrice;
	}
}
