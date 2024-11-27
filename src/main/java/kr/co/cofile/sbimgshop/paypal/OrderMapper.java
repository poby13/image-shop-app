package kr.co.cofile.sbimgshop.paypal;

import org.apache.ibatis.annotations.*;

@Mapper
public interface OrderMapper {
    @Insert("INSERT INTO orders (paypal_order_id, status, product_id, amount, created_at) " +
            "VALUES (#{paypalOrderId}, #{status}, #{productId}, #{amount}, #{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Order order);

    @Select("SELECT * FROM orders WHERE paypal_order_id = #{paypalOrderId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "product", column = "product_id",
                    one = @One(select = "kr.co.cofile.sbimgshop.paypal.ProductMapper.findById"))
    })
    Order findByPaypalOrderId(String paypalOrderId);

    @Update("UPDATE orders SET status = #{status} WHERE paypal_order_id = #{paypalOrderId}")
    void updateStatus(@Param("paypalOrderId") String paypalOrderId,
                      @Param("status") String status);
}