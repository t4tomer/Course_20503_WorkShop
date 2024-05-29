

-- ! create new Project_Data_Base  databse 
Create DataBase Project_Data_Base;

Create DAtaBase book;
-- ! delete databse
DROP database Project_Data_Base;




-- ! show Image Table from Project_Data_Base 
select * from Project_Data_Base.Image_Table;
select * from Project_Data_Base.Product_Table;    
select * from Project_Data_Base.Users_Table;   
select * from Project_Data_Base.Cart_Table;   






-- Update the balance
UPDATE Project_Data_Base.Users_Table
SET balance = 1400
WHERE email = 'tomer.polsky1@gmail.com';

-- ! delete all the raws of the Project_Data_Base.Image_Table
DELETE FROM Project_Data_Base.Image_Table;



-- ! delete all the raws of the Image_Data_Base.Product_Table
DELETE FROM Project_Data_Base.Product_Table;

