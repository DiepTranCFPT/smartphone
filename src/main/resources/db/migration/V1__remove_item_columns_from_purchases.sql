-- Remove legacy item columns from purchases table
ALTER TABLE purchases DROP COLUMN IF EXISTS item_name;
ALTER TABLE purchases DROP COLUMN IF EXISTS qty;
ALTER TABLE purchases DROP COLUMN IF EXISTS unit_price;
ALTER TABLE purchases DROP COLUMN IF EXISTS warranty_months;

