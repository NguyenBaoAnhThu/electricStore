DELIMITER //
CREATE TRIGGER trg_after_invoice_item_insert
    AFTER INSERT ON invoice_item
    FOR EACH ROW
BEGIN
    IF NEW.payment_status <> 'ĐÃ HỦY' THEN
    UPDATE products
    SET stock = COALESCE(stock, 0) + NEW.quantity
    WHERE product_code = NEW.product_code;
END IF;
END; //
DELIMITER ;


DELIMITER //
CREATE TRIGGER trg_after_invoice_item_update
    AFTER UPDATE ON invoice_item
    FOR EACH ROW
BEGIN
    IF OLD.payment_status <> NEW.payment_status THEN
        IF OLD.payment_status <> 'ĐÃ HỦY' AND NEW.payment_status = 'ĐÃ HỦY' THEN
    UPDATE products
    SET stock = COALESCE(stock, 0) - NEW.quantity
    WHERE product_code = NEW.product_code;
    ELSEIF OLD.payment_status = 'ĐÃ HỦY' AND NEW.payment_status <> 'ĐÃ HỦY' THEN
    UPDATE products
    SET stock = COALESCE(stock, 0) + NEW.quantity
    WHERE product_code = NEW.product_code;
END IF;
END IF;
END; //
DELIMITER ;
