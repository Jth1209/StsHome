package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import domain.entity.Item;

public class ItemDao {
	private JdbcTemplate jdbcTemplate;
	
	public ItemDao(DataSource datasource) {
		this.jdbcTemplate = new JdbcTemplate(datasource);
	}
	
	public Item selectById(Long id) {
		Item result = jdbcTemplate.queryForObject("select * from item where item_id = ?", 
				(ResultSet rs, int rowNum) -> {
					Item item = new Item(rs.getLong("item_id"),
							             rs.getString("name"),
							             rs.getInt("price"),
							             rs.getInt("stock_quantity"));
					return item;
				},id);
		return result;
	}
	
	public void inserItem(Item item) {//insert에서 가장 중요한 것은 KeyHolder와 new String[] {"autoIncrement 칼럼 명"} 이다.
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update((Connection con) -> {
			PreparedStatement pstmt = con.prepareStatement("insert into item (name,price,stock_quantity) values (?,?,?)",new String[] {"item_id"});
			pstmt.setString(1, item.getName());
			pstmt.setInt(2,item.getPrice());
			pstmt.setInt(3,item.getStockQuantity());
			return pstmt;
		},keyHolder);
		Number keyValue = keyHolder.getKey();
		item.setId(keyValue.longValue());
	}
	
	public void updateItem(Item item) {
		jdbcTemplate.update("update item set name = ?, price = ?, stock_quantity = ? where item_id = ?",
				            item.getName(),item.getPrice(),item.getStockQuantity(),item.getId());
	}
	
	public List<Item> selectAll(){
		List<Item> results = jdbcTemplate.query("select * from item", 
				(ResultSet rs , int rowNum)->{
					Item item = new Item(rs.getLong("item_id"),
							             rs.getString("name"),
							             rs.getInt("price"),
							             rs.getInt("stock_quantity"));
					return item;
				});
		return results;
	}
}
