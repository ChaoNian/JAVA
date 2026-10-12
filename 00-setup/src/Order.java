import java.math.BigDecimal;

/**
 * 最小订单模型：创建时待支付，只能从 PENDING 转到 PAID。
 * 金额用 BigDecimal，避免 double 丢精度。
 */
public class Order {
    /** 订单生命周期里允许出现的状态；非法取值编译期就能拦。 */
    public enum Status {
        PENDING,   // 待支付：刚创建
        PAID,      // 已支付：pay() 成功后
        CANCELLED  // 已取消：本文件尚未写 cancel 流转
    }

    private final long id;              // 订单号：造出来后不应改
    private final BigDecimal amount;    // 金额：造出来后不应改
    private Status status;              // 状态会变，所以不能 final

    /**
     * 建单：校验金额为正，初始状态固定为 PENDING。
     * amount.signum()：负数为 -1，零为 0，正数为 1。
     */
    public Order(long id, BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        this.id = id;
        this.amount = amount;
        this.status = Status.PENDING; // 新单一律待支付
    }

    /**
     * 支付：状态机里只允许 PENDING → PAID。
     * 已支付/已取消再 pay，属于非法流转，抛 IllegalStateException。
     */
    public void pay() {
        if (status != Status.PENDING) {
            throw new IllegalStateException("Order is not pending");
        }
        status = Status.PAID;
    }

    /** 只读查询当前状态；外部不能直接改字段。 */
    public Status getStatus() {
        return status;
    }
}
