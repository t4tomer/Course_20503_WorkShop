

-- ! create new image  databse 
Create Database Image_Data_Base

-- ! delete databse
DROP database Image_Data_Base;


-- ! show Image Table from Image_Data_Base 
-- use Image_Data_Base
select * from Image_Data_Base.Image_Table;
select * from Image_Data_Base.Product_Table;    

-- ! delete all the raws of the Image_Data_Base.Image_Table
DELETE FROM Image_Data_Base.Image_Table;



-- ! delete all the raws of the Image_Data_Base.Product_Table
DELETE FROM Image_Data_Base.Product_Table;
