CREATE INDEX index_user_product ON inventory (user_id, product_id);
CREATE INDEX index_origin_user_date ON inventory_movements(origin_user_id,date);
CREATE INDEX index_email ON user (email);



