
USE master;
GO
IF DB_ID('QLKS') IS NOT NULL
	DROP DATABASE QLKS
GO
CREATE DATABASE QLKS;
GO
USE QLKS;
GO
create function funcSDT
(
	@SoDT varchar(Max)
)
returns bit
as
	begin
	Declare @KQ bit;
	Set @KQ=0;
	Declare @So tinyint;
	Set @So=0;
	Declare @i tinyint;
	Set @i=0;
	While (@i<=len(@SoDT))
	begin
	if (charindex(substring(@SoDT,@i,1),'0123456789')!=0)
	Set @So=@So+1;
	Set @i=@i+1;
	end
	if (@SoDT='' or @So=10 or @So=11) -- Điều kiện kiểm tra số đt ở đây
	Set @KQ=1;
	return @KQ;
	END;
GO
DROP TABLE IF EXISTS tbl_nhanvien;
CREATE TABLE tbl_nhanvien
(
	maNV VARCHAR(30) CONSTRAINT kc_tbl_nhanvien PRIMARY KEY,
	tenNV VARCHAR(50) NOT NULL,
	ngaySinh DATE NOT NULL CONSTRAINT ck_tbl_nhanvien_ngaySinh CHECK(DATEDIFF(YEAR,ngaySinh,CAST(GETDATE() AS DATE)) >= 18),
	gioiTinh VARCHAR(5) NOT NULL CONSTRAINT ck_tbl_nhanvien_gioitinh CHECK(gioiTinh IN('Nam','Nu')),
	chucVu BIT NOT NULL,
	cmt VARCHAR(12) UNIQUE NOT NULL,
	sdt VARCHAR(11) UNIQUE NOT NULL CONSTRAINT ck_tbl_nhanvien_sdt CHECK(dbo.funcSDT(sdt)=1)
);
GO
DROP TABLE IF EXISTS tbl_taikhoan;
CREATE TABLE tbl_taikhoan
(
	maNV VARCHAR(30) CONSTRAINT kc_tbl_taikhoan PRIMARY KEY,
	taiKhoan VARCHAR(50) NOT NULL,
	matKhau VARCHAR(20) NOT NULL
);
GO
CREATE TABLE tbl_loaiphong
(
	maLoaiPhong VARCHAR(10) CONSTRAINT kc_tbl_loaiphong PRIMARY KEY,
	moTa VARCHAR(30) NOT NULL,
	gia MONEY NOT NULL CONSTRAINT ck_tbl_loaiphong_gia CHECK(gia > 0), -- gia theo ngay
	--donvi VARCHAR(10) NOT NULL DEFAULT 'vnd'
);
GO
CREATE TABLE tbl_phong
(
	maPhong VARCHAR(30) CONSTRAINT kc_tbl_phong PRIMARY KEY,
	tenPhong VARCHAR(50) NOT NULL,
	maLoaiPhong VARCHAR(10) NOT NULL,
	mota VARCHAR(50) NOT NULL,
	--trangthai VARCHAR(10) NOT NULL CONSTRAINT ck_tbl_phong_trangthai CHECK(trangthai IN('dung','baotri')),
	songuoitoida INT NOT NULL CONSTRAINT ck_tbl_phong_soluongNguoi CHECK(songuoitoida > 0)
);
GO
CREATE TABLE tbl_trangthaiphong
(
	maPhong VARCHAR(30) NOT NULL,
	ngay DATE NOT NULL,
	tinhTrang VARCHAR(20) NOT NULL DEFAULT 'trong' CONSTRAINT ck_tbl_trangthaiphong CHECK(tinhTrang IN('trong','dat','hen')),
	CONSTRAINT kc_tbl_trangthaiphong PRIMARY KEY(maPhong,ngay)
);
GO
CREATE TABLE tbl_khachhang
(
	maKH VARCHAR(30) CONSTRAINT kc_tbl_khachhang PRIMARY KEY,
	tenKH VARCHAR(50) NOT NULL,
	ngaySinh DATE NOT NULL CONSTRAINT ck_tbl_khachhang_ngaySinh CHECK(DATEDIFF(YEAR,ngaySinh,CAST(GETDATE() AS DATE)) >= 18),
	gioiTinh VARCHAR(5) NOT NULL CONSTRAINT ck_tbl_khachhang_gioitinh CHECK(gioiTinh IN('Nam','Nu')),
	cmt VARCHAR(12) UNIQUE NOT NULL,
	sdt VARCHAR(11) UNIQUE NOT NULL CONSTRAINT ck_tbl_khachhang_sdt CHECK(dbo.funcSDT(sdt)=1)
);
GO
CREATE TABLE tbl_doAn
(
	maDA VARCHAR(30) CONSTRAINT kc_tbl_doAn PRIMARY KEY,
	tenDA VARCHAR(100) NOT NULL,
	gia MONEY NOT NULL CONSTRAINT ck_tbl_doAn_gia CHECK(gia > 0),
	--donvi VARCHAR(10) NOT NULL DEFAULT 'vnd'
);
GO
CREATE TABLE tbl_doUong
(
	maDU VARCHAR(30) CONSTRAINT kc_tbl_doUong PRIMARY KEY,
	tenDU VARCHAR(100) NOT NULL,
	gia MONEY NOT NULL CONSTRAINT ck_tbl_doUong_gia CHECK(gia > 0),
	--donvi VARCHAR(10) NOT NULL DEFAULT 'vnd'
);
GO
CREATE TABLE tbl_dangkyDoAn
(
	ngay DATE NOT NULL DEFAULT CAST(GETDATE() AS DATE),
	maPhong VARCHAR(30) NOT NULL,
	maDA VARCHAR(30) NOT NULL,
	soLuong INT NOT NULL DEFAULT 1 CONSTRAINT ck_tbl_dangkyDoAn_soLuong CHECK(soLuong > 0),
	CONSTRAINT kc_tbl_dangkyDoAn PRIMARY KEY(ngay,maPhong,maDA)
);
GO
CREATE TABLE tbl_dangkyDoUong
(
	ngay DATE NOT NULL DEFAULT CAST(GETDATE() AS DATE),
	maPhong VARCHAR(30) NOT NULL,
	maDU VARCHAR(30) NOT NULL,
	soLuong INT NOT NULL DEFAULT 1 CONSTRAINT ck_tbl_dangkyDoUong_soLuong CHECK(soLuong > 0),
	CONSTRAINT kc_tbl_dangkyDoUong PRIMARY KEY(ngay,maPhong,maDU)
);
GO
CREATE TABLE tbl_datphong
(
	maKH VARCHAR(30) NOT NULL,
	maNV VARCHAR(30) NOT NULL,
	maPhong VARCHAR(30) NOT NULL,
	ngayDat DATE NOT NULL,
	ngayTra DATE NOT NULL,
	trangthai VARCHAR(10) DEFAULT 'dat' NOT NULL CONSTRAINT ck_tbl_datphong_trangthai CHECK(trangthai IN('dat','hen')),
	thanhtoan BIT DEFAULT 0 NOT NULL,
	CONSTRAINT kc_tbl_datPhong PRIMARY KEY(maKH,maNV,maPhong,ngayDat)
);
GO
ALTER TABLE dbo.tbl_taikhoan 
ADD
	CONSTRAINT kn_tbl_taikhoan_maNV
	FOREIGN KEY(maNV)
	REFERENCES dbo.tbl_nhanvien(maNV);
GO
ALTER TABLE dbo.tbl_phong
ADD
	CONSTRAINT kn_tbl_phong_maLoaiPhong
	FOREIGN KEY(maLoaiPhong)
	REFERENCES dbo.tbl_loaiphong(maLoaiPhong)
GO
ALTER TABLE dbo.tbl_trangthaiphong
ADD
	CONSTRAINT kn_tbl_trangthaiphong_maPhong
	FOREIGN KEY(maPhong)
	REFERENCES dbo.tbl_phong(maPhong)
GO
ALTER TABLE dbo.tbl_datphong
ADD
	CONSTRAINT kn_tbl_datphong_maKH
	FOREIGN KEY(maKH)
	REFERENCES dbo.tbl_khachhang(maKH)
GO
ALTER TABLE dbo.tbl_datphong
ADD
	CONSTRAINT kn_tbl_datphong_maNV
	FOREIGN KEY(maNV)
	REFERENCES dbo.tbl_nhanvien(maNV)
GO
ALTER TABLE dbo.tbl_datphong
ADD
	CONSTRAINT kn_tbl_datphong_maPhong
	FOREIGN KEY(maPhong)
	REFERENCES dbo.tbl_phong(maPhong)
GO
ALTER TABLE dbo.tbl_dangkyDoAn
ADD
	CONSTRAINT kn_tbl_dangkyDoAn_maPhong
	FOREIGN KEY(maPhong)
	REFERENCES dbo.tbl_phong(maPhong)
GO
ALTER TABLE dbo.tbl_dangkyDoAn
ADD
	CONSTRAINT kn_tbl_dangkyDoAn_maDA
	FOREIGN KEY(maDA)
	REFERENCES dbo.tbl_doAn(maDA)
GO
ALTER TABLE dbo.tbl_dangkyDoUong
ADD
	CONSTRAINT kn_tbl_dangkyDoUong_maPhong
	FOREIGN KEY(maPhong)
	REFERENCES dbo.tbl_phong(maPhong)
GO
ALTER TABLE dbo.tbl_dangkyDoUong
ADD
	CONSTRAINT kn_tbl_dangkyDoUong_maDU
	FOREIGN KEY(maDU)
	REFERENCES dbo.tbl_doUong(maDU)
GO
-- Kiểm tra đăng nhập
-- Tạo proc đăng nhập
-- proc ví dụ
CREATE PROCEDURE MyStoredProcedure AS
SET ROWCOUNT 2
SELECT * FROM dbo.tbl_nhanvien ORDER BY maNV
GO
-- proc them nhan vien
-- SELECT DATEDIFF(YEAR,'3/3/2002',CAST(GETDATE() AS DATE))
CREATE PROC addNhanVien(@maNV VARCHAR(30), @tenNV VARCHAR(50),
@date DATE, @sex VARCHAR(5), @cv BIT, @cmt VARCHAR(12), @sdt VARCHAR(11))
AS
BEGIN
    IF(@maNV='' OR @maNV IS NULL) PRINT('Ma nhan vien khong duoc trong')
	ELSE IF(EXISTS(SELECT*FROM dbo.tbl_nhanvien WHERE maNV IN(@maNV)))
		BEGIN
			--PRINT('Ma nhan vien ton tai')
		    select cast(-3 as int) as ketqua
		END
	ELSE IF(@tenNV='' OR @tenNV IS NULL) PRINT('Ten nhan vien khong duoc trong')
	ELSE IF(@date='' OR @date IS NULL) PRINT('Ngay sinh khong duoc trong')
	ELSE IF( DATEDIFF(YEAR,@date,CAST(GETDATE() AS DATE)) < 18)
		BEGIN
			--PRINT('Nhan vien chua du 18 tuoi')
			 select cast(-2 as int) as ketqua
		END
	ELSE IF(@sex NOT IN('Nam','Nu')) PRINT('Gioi tinh phai la Nam/Nu')
	ELSE IF(@cv NOT IN(0,1))
		BEGIN
			PRINT('Chuc vu phai la 0 hoac 1')
		END
	ELSE IF(@cmt='' OR @cmt IS NULL) PRINT('CMT khong duoc trong')
	ELSE IF(EXISTS(SELECT*FROM dbo.tbl_nhanvien WHERE cmt IN(@cmt)))
		BEGIN
		     --PRINT('CMT da ton tai')
			 select cast(-1 as int) as ketqua
		END
	ELSE IF(@sdt='' OR @sdt IS NULL) PRINT('So dien thoai khong duoc trong')
	ELSE IF(EXISTS(SELECT*FROM dbo.tbl_nhanvien WHERE sdt IN(@sdt)) OR dbo.funcSDT(@sdt)<>1)
		BEGIN
		    --PRINT('So dien thoai ton tai hoac nhap khong dung dinh dang');
			select cast(0 as int) as ketqua
		END
	ELSE
		BEGIN
			 INSERT INTO dbo.tbl_nhanvien(maNV,tenNV,ngaySinh,gioiTinh,chucVu,cmt,sdt)VALUES
			 (@maNV,@tenNV,@date,@sex,@cv,@cmt,@sdt)
			 --PRINT('Them nhan vien '+@tenNV+' thanh cong!');
			 select cast(1 as int) as ketqua
		END
END;
GO
  -- Thêm nhân viên:
EXEC dbo.addNhanVien @maNV = 'bibo', @tenNV = 'Nguyen Cong Binh', @date = '3/8/2001', @sex = 'Nam', @cv = 1, @cmt = '001201007344', @sdt = '0971912776'
GO
EXEC dbo.addNhanVien @maNV = 'ts', @tenNV = 'Nguyen Tung Son', @date = '3/8/2001', @sex = 'Nam', @cv = 1, @cmt = '091201207343', @sdt = '0818696268'
GO
EXEC dbo.addNhanVien @maNV = 'bth', @tenNV = 'Bui Thi Hanh', @date = '4/3/2001', @sex = 'Nu', @cv = 1, @cmt = '011201207544', @sdt = '0343066599'
GO
DECLARE @chucVu INT;
SET @chucVu=1;
SELECT * FROM dbo.tbl_nhanvien WHERE chucVu=@chucVu
GO
CREATE PROC updateNhanVien(@maNV VARCHAR(30), @tenNV VARCHAR(50),
@date DATE, @sex VARCHAR(5), @cv BIT, @cmt VARCHAR(12), @sdt VARCHAR(11))
AS
BEGIN
    IF(@maNV='' OR @maNV IS NULL) PRINT('Ma nhan vien khong duoc trong')
	ELSE IF(NOT EXISTS(SELECT*FROM dbo.tbl_nhanvien WHERE maNV IN(@maNV)))
		BEGIN
			--PRINT('Ma nhan vien khong ton tai')
		    select cast(-3 as int) as ketqua
		END
	ELSE IF(@tenNV='' OR @tenNV IS NULL) PRINT('Ten nhan vien khong duoc trong')
	ELSE IF(@date='' OR @date IS NULL) PRINT('Ngay sinh khong duoc trong')
	ELSE IF(DATEDIFF(YEAR,@date,CAST(GETDATE() AS DATE)) < 18)
		BEGIN
			--PRINT('Nhan vien chua du 18 tuoi')
			 select cast(-2 as int) as ketqua
		END
	ELSE IF(@sex NOT IN('Nam','Nu')) PRINT('Gioi tinh phai la Nam/Nu')
	ELSE IF(@cv NOT IN(0,1))
		BEGIN
			PRINT('Chuc vu phai la 0 hoac 1')
		END
	ELSE IF(@cmt='' OR @cmt IS NULL) PRINT('CMT khong duoc trong')
	ELSE IF((SELECT COUNT(*) FROM dbo.tbl_nhanvien WHERE maNV IN(@maNV) AND cmt IN(@cmt)) > 1)
		BEGIN
		     --PRINT('CMT da ton tai')
			 select cast(-1 as int) as ketqua
		END
	ELSE IF(@sdt='' OR @sdt IS NULL) PRINT('So dien thoai khong duoc trong')
	ELSE IF((SELECT COUNT(*) FROM dbo.tbl_nhanvien WHERE maNV IN(@maNV) AND sdt IN(@sdt)) > 1
			OR dbo.funcSDT(@sdt)<>1)
		BEGIN
		    --PRINT('So dien thoai ton tai hoac nhap khong dung dinh dang');
			select cast(0 as int) as ketqua
		END
	ELSE
		BEGIN
			 UPDATE dbo.tbl_nhanvien SET tenNV=@tenNV, ngaySinh=@date, gioiTinh=@sex, chucVu=@cv, cmt=@cmt, sdt=@sdt WHERE maNV IN(@maNV);
			 --PRINT('Cap nhat du lieu thanh cong!');
			 select cast(1 as int) as ketqua
		END
END;
GO
  -- Sửa thông tin nhân viên:
EXEC dbo.updateNhanVien @maNV = 'bth', @tenNV = 'Bui Thi Hanh', @date = '4/3/2001', @sex = 'Nu', @cv = 1, @cmt = '011201207544', @sdt = '0343066599'
GO
-- trigger đăng ký tài khoản (1 nhân viên / 1 tài khoản)
CREATE TRIGGER addAccount ON dbo.tbl_taikhoan INSTEAD OF INSERT
AS
BEGIN
	DECLARE @maNV VARCHAR(30);
	DECLARE @user VARCHAR(50);
	DECLARE @pass VARCHAR(20);
	SET @maNV = (SELECT DISTINCT Inserted.maNV FROM Inserted);
	SET @user = (SELECT DISTINCT Inserted.taiKhoan FROM Inserted);
	SET @pass = (SELECT DISTINCT Inserted.matKhau FROM Inserted);

	IF(@maNV='' OR @maNV IS NULL) PRINT('Ma nhan vien khong duoc trong')
	ELSE IF(@user='' OR @user IS NULL) PRINT('User khong duoc trong')
	ELSE IF(@pass='' OR @pass IS NULL) PRINT('Pass khong duoc trong')

	ELSE IF EXISTS (SELECT * FROM dbo.tbl_nhanvien WHERE maNV IN (@maNV))
		BEGIN
			IF EXISTS (SELECT * FROM dbo.tbl_taikhoan WHERE maNV IN (@maNV))
				BEGIN
					--PRINT(N'Mã nhân viên này đã được đăng ký -> Vui lòng nhập khác!');
					select cast(-1 as int) as ketqua
				END;
			ELSE
				BEGIN
					IF EXISTS (SELECT * FROM dbo.tbl_taikhoan WHERE taiKhoan IN (@user))
						BEGIN
							--PRINT(N'Tài khoản này đã tồn tại!');
							--ROLLBACK TRANSACTION;
							select cast(0 as int) as ketqua;
						END;
					ELSE
						BEGIN
							INSERT INTO dbo.tbl_taikhoan (maNV,taiKhoan,matKhau)VALUES(@maNV,@user,@pass);
							--PRINT('Them tai khoan ' +@user+ ' thanh cong!')
							select cast(1 as int) as ketqua;
						END
				END;
		END;
	ELSE
		BEGIN
		    -- PRINT(N'Nhân viên có mã ' + @maNV + N' không tồn tại');
			select cast(-2 as int) as ketqua;
		END
END
GO
-- Ví dụ thêm tài khoản nhân viên
INSERT INTO dbo.tbl_taikhoan(maNV,taiKhoan,matKhau)VALUES
('bibo','binhboong','1');
go
INSERT INTO dbo.tbl_taikhoan(maNV,taiKhoan,matKhau)VALUES
('ts','sontung123','123');
GO
INSERT INTO dbo.tbl_taikhoan(maNV,taiKhoan,matKhau)VALUES
('bth','hanhbui123','123456');
GO
SELECT * FROM dbo.tbl_taikhoan
GO
-- proc kiểm tra đăng nhập
CREATE PROC checkLogin
	@user VARCHAR(50),
	@pass VARCHAR(20)
AS
BEGIN
	--DECLARE @login INT; 
	IF (@user=''OR @user IS NULL OR @pass='' OR @pass IS NULL) 
	begin
		select cast(0 as int) as ketqua
		--PRINT('user/password khong duoc de trong!');
	end

	ELSE IF NOT EXISTS (SELECT * FROM dbo.tbl_taikhoan WHERE taiKhoan=@user)
		BEGIN
			--SET @login=0;
			--SELECT @login AS [login];
			select cast(0 as int) as ketqua
			-----------------------------------------------------------------------------
			--PRINT('Tai khoan '+@user+' khong ton tai -> Vui long dang ky!');
		END
	ELSE IF EXISTS (SELECT * FROM dbo.tbl_taikhoan WHERE taiKhoan=@user)
		BEGIN
			IF NOT EXISTS (SELECT * FROM dbo.tbl_taikhoan WHERE matKhau=@pass)
				BEGIN
					--SET @login=0;
					--SELECT @login AS [login];
					select cast(0 as int) as ketqua
					---------------------------------------------------------------------
					--PRINT('Mat khau sai -> Vui long nhap lai!');
				END
		ELSE
			BEGIN
				DECLARE @name NVARCHAR(50);
				SET @name=(SELECT tenNV FROM dbo.tbl_nhanvien INNER JOIN dbo.tbl_taikhoan
				ON tbl_taikhoan.maNV = tbl_nhanvien.maNV AND taiKhoan=@user);
				-------------------------------------------------------------------------
				--SET @login=1;
				--SELECT @login AS [login];
				select cast(1 as int) as ketqua
				--PRINT('Nhan vien ' + @name + ' dang nhap thanh cong!')
			END
		END
END
GO
-- Ví dụ kiểm tra đăng nhập
EXECUTE dbo.checkLogin @user='binhboong',@pass='1';
GO
-- trigger thay đổi mã Nhân viên
--CREATE TRIGGER changeMaNV ON dbo.tbl_nhanvien INSTEAD OF UPDATE
--AS
--BEGIN
--	DECLARE @sMaNV VARCHAR(30);
--	DECLARE @sMaNVTemple VARCHAR(30);
--	DECLARE @sMaNVUpdate VARCHAR(30);
--	SET @sMaNVTemple = 'templatewww';
--	SELECT @sMaNV =  (SELECT DISTINCT Deleted.maNV FROM Deleted);
--	SELECT @sMaNVUpdate = (SELECT DISTINCT Inserted.maNV FROM Inserted);

--	INSERT INTO dbo.tbl_nhanvien (maNV) VALUES (@sMaNVTemple);
--	UPDATE dbo.tbl_taikhoan SET maNV =  @sMaNVTemple WHERE maNV IN (@sMaNV); 
--	UPDATE dbo.tbl_datphong SET maNV = @sMaNVTemple WHERE maNV IN(@sMaNV);

--	UPDATE dbo.tbl_nhanvien SET maNV = @sMaNVUpdate WHERE maNV IN (@sMaNV);
--	UPDATE dbo.tbl_taikhoan SET maNV = @sMaNVUpdate WHERE maNV IN (@sMaNVTemple);
--	UPDATE dbo.tbl_datphong SET maNV = @sMaNVUpdate WHERE maNV IN(@sMaNVTemple);

--	DELETE FROM dbo.tbl_nhanvien WHERE maNV IN (@sMaNVTemple);
--END
--GO
--DROP TRIGGER dbo.changeMaNV;
--GO
-- proc them loai phong
CREATE PROC addLoaiPhong(@maLP VARCHAR(10), @moTa VARCHAR(30), @gia MONEY)
AS
BEGIN
	IF(@maLP='' OR @maLP IS NULL ) PRINT('Ma loai phong khong duoc trong')
	ELSE IF(EXISTS(SELECT*FROM dbo.tbl_loaiphong WHERE maLoaiPhong IN(@maLP)))
	begin
		--PRINT('Ma loai phong ton tai')
		select cast(-1 as int) as ketqua
	end
	
	ELSE IF(@moTa='' OR @moTa IS NULL) PRINT('Mo ta khong duoc trong')
	ELSE IF(@gia IS NULL OR @gia <= 0)
		BEGIN
		    --PRINT('Gia khong duoc trong va > 0')
			SELECT cast(0 as int) as ketqua
		END
	ELSE
		BEGIN
			INSERT INTO dbo.tbl_loaiphong(maLoaiPhong,moTa,gia)VALUES
			(@maLP,@moTa,@gia)
			--PRINT('Them loai phong ' +@maLP+ ' thanh cong!')
			select cast(1 as int) as ketqua
		END
END
GO
-- Thêm loại phòng
EXEC dbo.addLoaiPhong @maLP = 'vip', @moTa = 'Phong cao cap', @gia = 700
GO
EXEC dbo.addLoaiPhong @maLP = 'st1', @moTa = 'Phong tieu chuan loai 1', @gia = 500
GO
EXEC dbo.addLoaiPhong @maLP = 'st2', @moTa = 'Phong tieu chuan loai 2', @gia = 400
GO
CREATE PROC updateLoaiPhong(@maLP VARCHAR(10), @moTa VARCHAR(30), @gia MONEY)
AS
BEGIN
	IF(@maLP='' OR @maLP IS NULL ) PRINT('Ma loai phong khong duoc trong')
	ELSE IF(NOT EXISTS(SELECT*FROM dbo.tbl_loaiphong WHERE maLoaiPhong IN(@maLP)))
	begin
		--PRINT('Ma loai phong khong ton tai')
		select cast(-1 as int) as ketqua
	end
	
	ELSE IF(@moTa='' OR @moTa IS NULL) PRINT('Mo ta khong duoc trong')
	ELSE IF(@gia IS NULL OR @gia <= 0)
		BEGIN
		    --PRINT('Gia khong duoc trong va > 0')
			SELECT cast(0 as int) as ketqua
		END
	ELSE
		BEGIN
			UPDATE dbo.tbl_loaiphong SET moTa=@moTa, gia=@gia WHERE maLoaiPhong IN(@maLP)
			--PRINT('Cap nhat du lieu thanh cong!')
			select cast(1 as int) as ketqua
		END
END
GO
SELECT * FROM dbo.tbl_loaiphong
GO
-- proc them khach hang
CREATE PROC addKhachHang(@maKH VARCHAR(30), @tenKH VARCHAR(50), @date DATE,
@sex VARCHAR(5), @cmt VARCHAR(12), @sdt VARCHAR(11))
AS
BEGIN
	IF(@maKH='' OR @maKH IS NULL) PRINT('Ma khach hang khong duoc trong')
	ELSE IF(EXISTS(SELECT*FROM dbo.tbl_khachhang WHERE maKH IN(@maKH))) 
		BEGIN
			--PRINT('Ma khach hang ton tai');
			SELECT cast(-3 as int) as ketqua
		END
	ELSE IF(@tenKH='' OR @tenKH IS NULL) PRINT('Ten khach hang khong duoc trong')
	ELSE IF(@date='' OR @date IS NULL)
		BEGIN
			PRINT('Ngay sinh khong duoc trong')
		END
	ELSE IF( DATEDIFF(YEAR,@date,CAST(GETDATE() AS DATE)) < 18)
		BEGIN
			--PRINT('Nhan vien chua du 18 tuoi')
			 select cast(-2 as int) as ketqua
		END
	ELSE IF(@sex NOT IN('Nam','Nu')) PRINT('Gioi tinh phai la Nam/Nu')
	ELSE IF(@cmt='' OR @cmt IS NULL) PRINT('CMT khong duoc trong')
	ELSE IF(EXISTS(SELECT*FROM dbo.tbl_khachhang WHERE cmt IN(@cmt))) 
		BEGIN
			--PRINT('CMT da ton tai')
			SELECT cast(-1 as int) as ketqua
		END
	ELSE IF(@sdt='' OR @sdt IS NULL) PRINT('So dien thoai khong duoc trong')
	ELSE IF(EXISTS(SELECT*FROM dbo.tbl_khachhang WHERE sdt IN(@sdt)) OR dbo.funcSDT(@sdt)<>1)
		BEGIN
		    --PRINT('So dien thoai da ton tai hoac nhap khong dung dinh dang');
			select cast(0 as int) as ketqua
		END
	ELSE
		BEGIN
			 INSERT INTO dbo.tbl_khachhang(maKH,tenKH,ngaySinh,gioiTinh,cmt,sdt)VALUES
			 (@maKH,@tenKH,@date,@sex,@cmt,@sdt)
			 select cast(1 as int) as ketqua
			 --PRINT('Them khach hang ' +@tenKH+ ' thanh cong!')
		END	
END
GO
-- Thêm khách hàng
EXEC dbo.addKhachHang @maKH = 'kh01', @tenKH = 'Bui Minh Tuan', @date = '3/2/2001', @sex = 'Nam', @cmt = '031412507344', @sdt = '0375912776'
GO
EXEC dbo.addKhachHang @maKH = 'kh02', @tenKH = 'Nguyen Quang Nam', @date = '2/1/2001', @sex = 'Nam', @cmt = '021412507344', @sdt = '0373612776'
GO
EXEC dbo.addKhachHang @maKH = 'kh03', @tenKH = 'Bui Minh Anh', @date = '10/2/2001', @sex = 'Nam', @cmt = '031412507374', @sdt = '0975912776'
GO
EXEC dbo.addKhachHang @maKH = 'kh04', @tenKH = 'Bui Thi Cam Nhung', @date = '11/12/2001', @sex = 'Nu', @cmt = '032412507444', @sdt = '0905912776'
GO
CREATE PROC updateKhachHang(@maKH VARCHAR(30), @tenKH VARCHAR(50), @date DATE,
@sex VARCHAR(5), @cmt VARCHAR(12), @sdt VARCHAR(11))
AS
BEGIN
	IF(@maKH='' OR @maKH IS NULL) PRINT('Ma khach hang khong duoc trong')
	ELSE IF(NOT EXISTS(SELECT*FROM dbo.tbl_khachhang WHERE maKH IN(@maKH))) 
		BEGIN
			--PRINT('Ma khach khong hang ton tai');
			SELECT cast(-3 as int) as ketqua
		END
	ELSE IF(@tenKH='' OR @tenKH IS NULL) PRINT('Ten khach hang khong duoc trong')
	ELSE IF(@date='' OR @date IS NULL)
		BEGIN
			PRINT('Ngay sinh khong duoc trong')
		END
	ELSE IF( DATEDIFF(YEAR,@date,CAST(GETDATE() AS DATE)) < 18)
		BEGIN
			--PRINT('Nhan vien chua du 18 tuoi')
			 select cast(-2 as int) as ketqua
		END
	ELSE IF(@sex NOT IN('Nam','Nu')) PRINT('Gioi tinh phai la Nam/Nu')
	ELSE IF(@cmt='' OR @cmt IS NULL) PRINT('CMT khong duoc trong')
	ELSE IF((SELECT COUNT(*) FROM dbo.tbl_khachhang WHERE maKH IN(@maKH) AND cmt IN(@cmt)) > 1) 
		BEGIN
			--PRINT('CMT da ton tai')
			SELECT cast(-1 as int) as ketqua
		END
	ELSE IF(@sdt='' OR @sdt IS NULL) PRINT('So dien thoai khong duoc trong')
	ELSE IF((SELECT COUNT(*) FROM dbo.tbl_khachhang WHERE maKH IN(@maKH) AND sdt IN(@sdt)) > 1 
			OR dbo.funcSDT(@sdt)<>1)
		BEGIN
		    --PRINT('So dien thoai da ton tai hoac nhap khong dung dinh dang');
			select cast(0 as int) as ketqua
		END
	ELSE
		BEGIN
			 UPDATE dbo.tbl_khachhang SET tenKH=@tenKH, ngaySinh=@date, gioiTinh=@sex, cmt=@cmt, sdt=@sdt WHERE maKH IN(@maKH)
			 select cast(1 as int) as ketqua
			 --PRINT('Cap nhat du lieu thanh cong!')
		END	
END
GO
-- Ví dụ khi sửa thông tin khách hàng
EXEC dbo.updateKhachHang @maKH = 'kh01', @tenKH = 'Bui Minh Tuan', @date = '3/2/2001', @sex = 'Nam', @cmt = '031412507344', @sdt = '0375912776'
GO
SELECT * FROM dbo.tbl_khachhang;
GO
-- select cast(getdate() as date);
-- trigger them phong
CREATE TRIGGER addphong ON dbo.tbl_phong
INSTEAD OF INSERT AS
BEGIN
	DECLARE @maPhong VARCHAR(30);
	DECLARE @tenphong VARCHAR(50);
	DECLARE @maloaiphong VARCHAR(10);
	DECLARE @mota VARCHAR(50);
	DECLARE @songuoimax INT;
	SET @maPhong = (SELECT DISTINCT Inserted.maPhong FROM Inserted);
	SET @tenphong = (SELECT DISTINCT Inserted.tenPhong FROM Inserted);
	SET @maloaiphong = (SELECT DISTINCT Inserted.maLoaiPhong FROM Inserted);
	SET @mota = (SELECT DISTINCT Inserted.mota FROM Inserted);
	SET @songuoimax = (SELECT DISTINCT Inserted.songuoitoida FROM Inserted);

	IF(@maPhong='' OR @maPhong IS NULL) PRINT('Ma phong khong duoc trong')
	ELSE IF(@tenphong='' OR @tenphong IS NULL) PRINT('Ten phong khong duoc trong')
	ELSE IF(@maloaiphong='' OR @maloaiphong IS NULL) PRINT('Ma loai phong khong duoc trong')
	ELSE IF(NOT EXISTS(SELECT*FROM dbo.tbl_loaiphong WHERE maLoaiPhong IN(@maloaiphong)))
		BEGIN
	    --PRINT('Loai phong '+@maloaiphong+' khong ton tai');
		SELECT cast(-2 as int) as ketqua
		END
	ELSE IF(@mota='' OR @mota IS NULL) PRINT('Mo ta khong duoc trong')
	ELSE IF(@songuoimax IS NULL OR @songuoimax <= 0) 
		BEGIN
			--PRINT('Dau vao so luong nguoi max khong dung'); 
			SELECT cast(-1 as int) as ketqua
		END

	ELSE IF EXISTS (SELECT * FROM dbo.tbl_phong WHERE maPhong IN (@maPhong))
		BEGIN
		    --PRINT(N'Phòng có mã ' + @maPhong + N' đã được thêm trước đó' + CHAR(13) + N'Thêm phòng không thành công');
			SELECT cast(0 as int) as ketqua
		END
	ELSE
		BEGIN
			INSERT INTO dbo.tbl_phong(maPhong,tenPhong,maLoaiPhong,mota,songuoitoida)VALUES
			(@maPhong,@tenphong,@maloaiphong,@mota,@songuoimax);
			INSERT INTO dbo.tbl_trangthaiphong(maPhong,ngay,tinhTrang)VALUES
			(@maPhong,GETDATE(),'trong');
			--PRINT('Them phong ' +@tenphong+ ' thanh cong!')
			--PRINT('Them trang thai trong cho phong thanh cong!')
			select cast(1 as int) as ketqua
		END
END
GO
-- Thêm phòng
INSERT INTO dbo.tbl_phong(maPhong,tenPhong,maLoaiPhong,mota,songuoitoida)VALUES
('a1', '10a1','vip','phong tien nghi view dep',5);
GO
INSERT INTO dbo.tbl_phong(maPhong,tenPhong,maLoaiPhong,mota,songuoitoida)VALUES
('a2', '10a2','vip','phong tien nghi view dep',5);
GO
INSERT INTO dbo.tbl_phong(maPhong,tenPhong,maLoaiPhong,mota,songuoitoida)VALUES
('a3', '10a3','vip','phong tien nghi view dep',5);
GO
INSERT INTO dbo.tbl_phong(maPhong,tenPhong,maLoaiPhong,mota,songuoitoida)VALUES
('a4', '10a4','vip','phong tien nghi view dep',5);
GO
INSERT INTO dbo.tbl_phong(maPhong,tenPhong,maLoaiPhong,mota,songuoitoida)VALUES
('b1', '10b1','st1','phong tien nghi',3);
GO
INSERT INTO dbo.tbl_phong(maPhong,tenPhong,maLoaiPhong,mota,songuoitoida)VALUES
('b2', '10b2','st1','phong tien nghi',3)
GO
SELECT * FROM dbo.tbl_phong
GO
SELECT * FROM dbo.tbl_trangthaiphong
GO
SELECT numberMaPhong.maPhong FROM (
  SELECT
    ROW_NUMBER() OVER (ORDER BY maPhong ASC) AS rownumber,
    maPhong
  FROM  (SELECT DISTINCT maPhong FROM tbl_trangthaiphong) AS a
) AS numberMaPhong WHERE numberMaPhong.rownumber=1
GO
SELECT DISTINCT cb.maPhong FROM dbo.tbl_trangthaiphong AS cb INNER JOIN
(SELECT maPhong, Max(ngayTra) AS maxngay FROM dbo.tbl_datphong GROUP BY maPhong HAVING Max(ngayTra) > CAST(GETDATE() AS DATE)) AS ba ON ba.maPhong = cb.maPhong
AND cb.tinhTrang IN('dat','tra')
GO
-- proc updateData (cho chay khi load app)
CREATE PROC updateData
AS
BEGIN
	UPDATE dbo.tbl_trangthaiphong SET ngay=CAST(GETDATE() AS DATE) WHERE tinhTrang='trong' AND ngay < CAST(GETDATE() AS date);
	UPDATE dbo.tbl_trangthaiphong SET tinhTrang='dat' WHERE tinhTrang='hen' AND ngay <= CAST(GETDATE() AS date);
	UPDATE dbo.tbl_datphong SET trangthai='dat' WHERE ngayDat <= CAST(GETDATE() AS date) AND trangthai='hen';
END
GO
-- proc them trang thai trong cho 1 phong voi tham so phong truyen vao (neu hien tai phong do khong co ai dat)
CREATE PROC uPhongTrong (@maphong VARCHAR(30))
AS
	 BEGIN
		IF (NOT EXISTS(SELECT maPhong FROM dbo.tbl_datphong WHERE maPhong IN(@maphong) GROUP BY maPhong HAVING Max(ngayTra) > CAST(GETDATE() AS DATE))
					AND NOT EXISTS (SELECT * FROM dbo.tbl_trangthaiphong WHERE maPhong IN(@maphong) AND tinhTrang='trong')
					AND EXISTS(SELECT*FROM dbo.tbl_phong WHERE maPhong IN(@maphong)))
			BEGIN
				INSERT INTO dbo.tbl_trangthaiphong(maPhong,ngay,tinhTrang)VALUES
				(@maphong, CAST(GETDATE() AS DATE),'trong')
				--PRINT('Them trang thai trong cho phong ' +@maphong+ ' thanh cong')
			END
		--ELSE 
		--	PRINT('Them trang thai trong khong thanh cong')
	 END
GO
EXEC dbo.uPhongTrong @maphong = 'a1'
GO
-- proc duyet tat ca cac phong -> them trang thai trong cho phong (neu hien tai phong do khong co ai dat) --> chay khi load app (trong proc co chua proc updateData)
CREATE PROC uPhongTrongs
@cnt INT
AS
BEGIN
	EXEC dbo.updateData;
	BEGIN
		WHILE (@cnt <= (SELECT COUNT(*) FROM dbo.tbl_phong))
			BEGIN
				DECLARE @maPhongTemp VARCHAR(30);
				SET @maPhongTemp = (SELECT b.maPhong FROM (SELECT ROW_NUMBER() OVER (ORDER BY maPhong ASC) AS rownumber, maPhong
					FROM (SELECT DISTINCT maPhong FROM dbo.tbl_phong) AS a
					) AS b WHERE b.rownumber=@cnt)
				EXEC dbo.uPhongTrong @maphong = @maPhongTemp;
				-- PRINT('cnt' + CAST(@cnt AS VARCHAR(100)))
				SET @cnt = @cnt + 1;
			END;
	END
END
GO
EXEC dbo.uPhongTrongs @cnt = 1
GO
-- proc đặt phòng (them vao bang datphong)
CREATE PROC datphong(@makh VARCHAR(30), @manv VARCHAR(30), @maphong VARCHAR(30), @ngaydat DATE, @ngaytra DATE)
AS
BEGIN
 EXEC dbo.updateData;
 EXEC dbo.uPhongTrong @maphong;
 
 IF (@makh='' OR @makh IS NULL) PRINT('Ma khach hang khong duoc de trong');
 ELSE IF (NOT EXISTS(SELECT * FROM dbo.tbl_khachhang WHERE maKH IN(@makh)))
	BEGIN
	     --PRINT('Hay nhap dung ma khach hang')
		 select cast(-6 as int) as ketqua
	END
 ELSE IF (@manv='' OR @manv IS NULL) PRINT('Ma nhan vien khong duoc de trong');
 ELSE IF (NOT EXISTS(SELECT * FROM dbo.tbl_nhanvien WHERE maNV IN(@manv)))
	BEGIN
	    --PRINT('Hay nhap dung ma nhan vien')
		select cast(-5 as int) as ketqua
	END
 ELSE IF (@maphong='' OR @maphong IS NULL) PRINT('Ma phong khong duoc de trong');
 ELSE IF (NOT EXISTS(SELECT * FROM dbo.tbl_phong WHERE maPhong IN(@maphong)))
	BEGIN
	     --PRINT('Hay nhap dung ma phong')
		 select cast(-4 as int) as ketqua
	END
 ELSE IF (@ngaydat='' OR @ngaydat IS NULL) PRINT('Ngay dat khong duoc de trong');
 ELSE IF (@ngaytra='' OR @ngaytra IS NULL) PRINT('Ngay tra khong duoc de trong');	
 ELSE IF(@ngaydat < CAST(GETDATE() AS DATE))
	BEGIN
		 --PRINT('Ngay dat phai la hien tai/tuong lai');
		 select cast(-3 as int) as ketqua
	END
 ELSE IF (@ngaytra <= @ngaydat)
  BEGIN
   --PRINT('Ngay tra phai >= ngaydat + 1');
   select cast(-2 as int) as ketqua
 --  IF (@ngaydat < CAST(GETDATE() AS date))
	--BEGIN
	--    PRINT('Thoi gian dat phai la hien tai/tuong lai');
	--END
  END;
-------------------------------------------------------
 ELSE IF (EXISTS (SELECT * FROM dbo.tbl_trangthaiphong WHERE tinhTrang='trong' AND maPhong IN(@maphong)))
  BEGIN 
   IF (@ngaydat = CAST(GETDATE() AS date))
    BEGIN
	 INSERT INTO tbl_datphong(maKH,maNV,maPhong,ngayDat,ngayTra,trangthai)VALUES
	(@makh,@manv,@maphong,@ngaydat,@ngaytra,'dat');
	 UPDATE dbo.tbl_trangthaiphong SET tinhTrang='dat' WHERE maPhong IN (@maphong) AND ngay=@ngaydat AND tinhTrang='trong'
	 select cast(1 as int) as ketqua
	 --PRINT('Dat phong '+@maphong+' thanh cong')
	END
   -------------------------------------------------------
   ELSE IF (@ngaydat > CAST(GETDATE() AS date))
    BEGIN
	 INSERT INTO tbl_datphong(maKH,maNV,maPhong,ngayDat,ngayTra,trangthai)VALUES
	(@makh,@manv,@maphong,@ngaydat,@ngaytra,'hen');
	 UPDATE dbo.tbl_trangthaiphong SET ngay=@ngaydat,tinhTrang='hen' WHERE maPhong IN (@maphong) AND tinhTrang='trong'
	 select cast(2 as int) as ketqua
     --PRINT('Hen dat phong '+@maphong+' thanh cong')
	END
  END
  -------------------------------------------------------
 ELSE IF (EXISTS (SELECT * FROM dbo.tbl_trangthaiphong WHERE tinhTrang IN('dat','hen') AND maPhong IN(@maphong)))
  BEGIN
	IF (@ngaydat = CAST(GETDATE() AS DATE))
		BEGIN
		    --PRINT('Dat trong khoang time giua')
			DECLARE @timedatmax DATE;
			DECLARE @timehenmin DATE;
			SET @timehenmin=(SELECT TOP 1 ngayDat FROM dbo.tbl_datphong WHERE maPhong IN(@maphong) AND trangthai='hen' ORDER BY ngayTra ASC);
			SET @timedatmax=(SELECT TOP 1 ngayTra FROM dbo.tbl_datphong WHERE maPhong IN(@maphong) AND trangthai='dat' ORDER BY ngayTra DESC);
			if(@timedatmax is null)
				begin
					if(@ngaytra < @timehenmin)
					begin
						INSERT INTO tbl_datphong(maKH,maNV,maPhong,ngayDat,ngayTra,trangthai)VALUES
						(@makh,@manv,@maphong,@ngaydat,@ngaytra,'dat');
						INSERT INTO dbo.tbl_trangthaiphong(maPhong,ngay,tinhTrang)VALUES(@maphong,@ngaydat,'dat');
						select cast(1 as int) as ketqua
						--PRINT('Dat phong '+@maphong+' thanh cong')
					end
				end
			else IF ((@ngaydat > @timedatmax) AND (@ngaytra < @timehenmin))
				BEGIN
					INSERT INTO tbl_datphong(maKH,maNV,maPhong,ngayDat,ngayTra,trangthai)VALUES
					(@makh,@manv,@maphong,@ngaydat,@ngaytra,'dat');
					INSERT INTO dbo.tbl_trangthaiphong(maPhong,ngay,tinhTrang)VALUES(@maphong,@ngaydat,'dat');
					select cast(1 as int) as ketqua
					--PRINT('Dat phong '+@maphong+' thanh cong')
				END
			ELSE
				begin
					select cast(-1 as int) as ketqua
					--PRINT('Phong '+@maphong+' hien tai chua dat duoc')
				end
		END
	-------------------------------------------------------
	ELSE IF (@ngaydat > CAST(GETDATE() AS DATE))
		BEGIN
		   DECLARE @timehenmax DATE; -- dat trong tuong lai
		   SET @timehenmax=(SELECT TOP 1 ngayTra FROM dbo.tbl_datphong WHERE maPhong IN(@maphong) ORDER BY ngayTra DESC);
		   IF ((select datediff(day,@timehenmax, @ngaydat) as timeDiff) < 1) PRINT('Dat phong '+@maphong+' khong thanh cong');
		   --------------------------------------------------------
		   ELSE
			BEGIN
				INSERT INTO tbl_datphong(maKH,maNV,maPhong,ngayDat,ngayTra,trangthai)VALUES
				(@makh,@manv,@maphong,@ngaydat,@ngaytra,'hen');
				INSERT INTO dbo.tbl_trangthaiphong(maPhong,ngay,tinhTrang)VALUES(@maphong,@ngaydat,'hen');
				select cast(2 as int) as ketqua
				--PRINT('Hen dat phong '+@maphong+' thanh cong')
			END
		END
  END
-------------------------------------------------------
 ELSE 
  BEGIN
	select cast(0 as int) as ketqua
	--PRINT('Dat phong '+@maphong+' khong thanh cong')
  END
END
GO
-- Ví dụ về đặt/hẹn phòng
EXEC dbo.datphong @makh = 'kh01',@manv = 'bibo',@maphong = 'a1',@ngaydat = '2023-1-2',@ngaytra = '2023-10-1'
GO
EXEC dbo.datphong @makh = 'kh01',@manv = 'ts',@maphong = 'a1',@ngaydat = '2023-10-2',@ngaytra = '2023-10-20'
GO
EXEC dbo.datphong @makh = 'kh01',@manv = 'bth',@maphong = 'a3',@ngaydat = '2023-1-2',@ngaytra = '2023-1-30'
GO
EXEC dbo.datphong @makh = 'kh02',@manv = 'bibo',@maphong = 'a4',@ngaydat = '2023-2-2',@ngaytra = '2023-2-10'
GO
EXEC dbo.datphong @makh = 'kh02',@manv = 'bibo',@maphong = 'a4',@ngaydat = '2023-2-11',@ngaytra = '2023-2-20'
GO
EXEC dbo.datphong @makh = 'kh02',@manv = 'bibo',@maphong = 'b1',@ngaydat = '2023-1-2',@ngaytra = '2023-1-20'
GO
EXEC dbo.datphong @makh = 'kh02',@manv = 'bibo',@maphong = 'b1',@ngaydat = '2023-1-21',@ngaytra = '2023-2-10'
GO
EXEC dbo.datphong @makh = 'kh02',@manv = 'bibo',@maphong = 'b1',@ngaydat = '2023-2-11',@ngaytra = '2023-2-20'
GO
SELECT * FROM dbo.tbl_datphong
GO
SELECT * FROM dbo.tbl_trangthaiphong
GO
-- proc them do an
CREATE PROC addDoAn(@maDoAn VARCHAR(30), @tenDA VARCHAR(100), @gia INT)
AS
BEGIN
	IF(@maDoAn='' OR @maDoAn IS NULL) PRINT('Ma do an khong duoc de trong')
	ELSE IF(EXISTS (SELECT * FROM dbo.tbl_doAn WHERE maDA IN(@maDoAn)))
		BEGIN
		     --PRINT('Ma do an ton tai -> Chen do an that bai')
			 select cast(-1 as int) as ketqua
		END
	ELSE IF(@tenDA='' OR @tenDA IS NULL) PRINT('Ten do an khong duoc de trong')
	ELSE IF(@gia IS NULL OR @gia <= 0)
		BEGIN
		     --PRINT('Gia mon an phai duong')
			 select cast(0 as int) as ketqua
		END
	ELSE 
		BEGIN
			INSERT INTO dbo.tbl_doAn(maDA,tenDA,gia)VALUES
			(@maDoAn,@tenDA,@gia);
			--PRINT('Them mon an '+@tenDA+' thanh cong!');
			select cast(1 as int) as ketqua
		END;
END;
GO
-- Thêm đồ ăn
EXEC dbo.addDoAn @maDoAn = 'gahap', @tenDA = 'ga hap', @gia = 150
GO
EXEC dbo.addDoAn @maDoAn = 'chimquay', @tenDA = 'chim cut quay', @gia = 100
GO
EXEC dbo.addDoAn @maDoAn = 'tomhap', @tenDA = 'tom cang xanh hap', @gia = 200
GO
EXEC dbo.addDoAn @maDoAn = 'cachien', @tenDA = 'ca chep chien gion', @gia = 80
GO
EXEC dbo.addDoAn @maDoAn = 'boxao', @tenDA = 'thit bo xao toi', @gia = 150
GO
CREATE PROC updateDoAn(@maDoAn VARCHAR(30), @tenDA VARCHAR(100), @gia INT)
AS
BEGIN
	IF(@maDoAn='' OR @maDoAn IS NULL) PRINT('Ma do an khong duoc de trong')
	ELSE IF(NOT EXISTS (SELECT * FROM dbo.tbl_doAn WHERE maDA IN(@maDoAn)))
		BEGIN
		     --PRINT('Ma do an khong ton tai')
			 select cast(-1 as int) as ketqua
		END
	ELSE IF(@tenDA='' OR @tenDA IS NULL) PRINT('Ten do an khong duoc de trong')
	ELSE IF(@gia IS NULL OR @gia <= 0)
		BEGIN
		     --PRINT('Gia mon an phai duong')
			 select cast(0 as int) as ketqua
		END
	ELSE 
		BEGIN
			UPDATE dbo.tbl_doAn SET tenDA=@tenDA, gia=@gia WHERE maDA IN (@maDoAn)
			--PRINT('Cap nhat du lieu thanh cong!');
			select cast(1 as int) as ketqua
		END;
END;
GO
SELECT * FROM dbo.tbl_doAn
GO
CREATE PROC addDoUong(@maDoUong VARCHAR(30), @tenDoUong VARCHAR(100), @gia INT)
AS
BEGIN
	IF(@maDoUong='' OR @maDoUong IS NULL) PRINT('Ma do uong khong duoc de trong')
	ELSE IF(EXISTS (SELECT * FROM dbo.tbl_doUong WHERE maDU IN(@maDoUong)))
		BEGIN
		     --PRINT('Ma do uong ton tai -> Chen do uong that bai')
			  select cast(-1 as int) as ketqua
		END
	ELSE IF(@tenDoUong='' OR @tenDoUong IS NULL) PRINT('Ten do uong khong de trong')
	ELSE IF(@gia IS NULL OR @gia <= 0) 
		BEGIN
		    --PRINT('Gia do uong phai duong')
			SELECT cast(0 as int) as ketqua
		END
	ELSE
		BEGIN
			INSERT INTO dbo.tbl_doUong(maDU,tenDU,gia)VALUES
			(@maDoUong,@tenDoUong,@gia);
			--PRINT('Them do uong '+@tenDoUong+' thanh cong!')
			select cast(1 as int) as ketqua
		END;
END;
GO
-- Ví dụ thêm đồ uống
EXEC dbo.addDoUong @maDoUong = 'cep', @tenDoUong = 'nuoc chanh ep', @gia = 10
GO
EXEC dbo.addDoUong @maDoUong = 'camv', @tenDoUong = 'nuoc cam vat', @gia = 20
GO
EXEC dbo.addDoUong @maDoUong = 'stbo', @tenDoUong = 'sinh to bo', @gia = 50
GO
EXEC dbo.addDoUong @maDoUong = 'nl', @tenDoUong = 'nuoc loc', @gia = 5
GO
EXEC dbo.addDoUong @maDoUong = 'cafed', @tenDoUong = 'cafe den', @gia = 30
GO
EXEC dbo.addDoUong @maDoUong = 'cafes', @tenDoUong = 'cafe sua', @gia = 10
GO
CREATE PROC updateDoUong(@maDoUong VARCHAR(30), @tenDoUong VARCHAR(100), @gia INT)
AS
BEGIN
	IF(@maDoUong='' OR @maDoUong IS NULL) PRINT('Ma do uong khong duoc de trong')
	ELSE IF(NOT EXISTS (SELECT * FROM dbo.tbl_doUong WHERE maDU IN(@maDoUong)))
		BEGIN
		     --PRINT('Ma do uong khong ton tai')
			  select cast(-1 as int) as ketqua
		END
	ELSE IF(@tenDoUong='' OR @tenDoUong IS NULL) PRINT('Ten do uong khong de trong')
	ELSE IF(@gia IS NULL OR @gia <= 0) 
		BEGIN
		    --PRINT('Gia do uong phai duong')
			SELECT cast(0 as int) as ketqua
		END
	ELSE
		BEGIN
			UPDATE dbo.tbl_doUong SET tenDU=@tenDoUong, gia=@gia WHERE maDU IN(@maDoUong);
			--PRINT('Cap nhat du lieu thanh cong!')
			select cast(1 as int) as ketqua
		END;
END;
GO
SELECT * FROM dbo.tbl_doUong
GO
CREATE PROC datDoAn(@maPhong VARCHAR(30), @maDoAn VARCHAR(30), @soluong INT) 
AS
BEGIN
	DECLARE @ngay DATE = CAST(GETDATE() AS DATE)
	IF(@maPhong='' OR @maPhong IS NULL) PRINT('Ma phong khong duoc trong')
	ELSE IF(@maDoAn='' OR @maDoAn IS NULL) PRINT('Ma do an khong duoc trong')
	ELSE IF(NOT EXISTS(SELECT*FROM dbo.tbl_doAn WHERE maDA IN(@maDoAn)))
		BEGIN
			--PRINT('Do an nhap vao khong ton tai -> dat do an that bai')
			SELECT cast(-2 as int) as ketqua
		END
	ELSE IF(@soluong IS NULL OR @soluong <= 0)
		BEGIN
			--PRINT('So luong phai duong')
			SELECT cast(-1 as int) as ketqua
		END
	ELSE IF(NOT EXISTS(SELECT*FROM dbo.tbl_datphong WHERE maPhong IN(@maPhong) AND ngayTra > CAST(GETDATE() AS date) AND trangthai='dat'))
		BEGIN
		    --PRINT('Ma phong khong hop le(phong trong/khong ton tai,...)')
			select cast(0 as int) as ketqua
		END
	ELSE IF(EXISTS(SELECT*FROM dbo.tbl_dangkyDoAn WHERE ngay=@ngay AND maPhong=@maPhong AND maDA=@maDoAn))
		BEGIN
		    UPDATE dbo.tbl_dangkyDoAn SET soLuong=soLuong+@soluong WHERE ngay=@ngay AND maPhong=@maPhong AND maDA=@maDoAn
			--PRINT('Dat do an thanh cong!')
			select cast(1 as int) as ketqua
		END
	ELSE
		BEGIN
			INSERT INTO dbo.tbl_dangkyDoAn(ngay,maPhong,maDA,soLuong)VALUES
			(@ngay,@maPhong,@maDoAn,@soluong);
			--PRINT('Dat do an thanh cong!')
			select cast(1 as int) as ketqua
		END
END
GO
-- Ví dụ đặt đồ ăn
EXEC dbo.datDoAn @maPhong = 'a1', @maDoAn = 'tomhap', @soluong = 3
GO
EXEC dbo.datDoAn @maPhong = 'a1', @maDoAn = 'boxao', @soluong = 3
GO
EXEC dbo.datDoAn @maPhong = 'a1', @maDoAn = 'cachien', @soluong = 3
GO
EXEC dbo.datDoAn @maPhong = 'a1', @maDoAn = 'gahap', @soluong = 3
GO
EXEC dbo.datDoAn @maPhong = 'a3', @maDoAn = 'tomhap', @soluong = 3
GO
EXEC dbo.datDoAn @maPhong = 'a3', @maDoAn = 'boxao', @soluong = 3
GO
EXEC dbo.datDoAn @maPhong = 'a3', @maDoAn = 'cachien', @soluong = 3
GO
EXEC dbo.datDoAn @maPhong = 'a3', @maDoAn = 'gahap', @soluong = 3
GO
EXEC dbo.datDoAn @maPhong = 'b1', @maDoAn = 'tomhap', @soluong = 3
GO
EXEC dbo.datDoAn @maPhong = 'b1', @maDoAn = 'boxao', @soluong = 3
GO
EXEC dbo.datDoAn @maPhong = 'b1', @maDoAn = 'cachien', @soluong = 3
GO
EXEC dbo.datDoAn @maPhong = 'b1', @maDoAn = 'gahap', @soluong = 3
GO
SELECT * FROM dbo.tbl_dangkyDoAn
GO
CREATE PROC datDoUong(@maPhong VARCHAR(30), @maDoUong VARCHAR(30), @soluong INT) 
AS
BEGIN
	DECLARE @ngay DATE = CAST(GETDATE() AS DATE)
	IF(@maPhong='' OR @maPhong IS NULL) PRINT('Ma phong khong duoc trong')
	ELSE IF(@maDoUong='' OR @maDoUong IS NULL) PRINT('Ma do uong khong duoc trong')
	ELSE IF(NOT EXISTS(SELECT*FROM dbo.tbl_doUong WHERE maDU IN(@maDoUong)))
		BEGIN
		   	--PRINT('Do uong nhap vao khong ton tai -> dat do uong that bai') 
			SELECT cast(-2 as int) as ketqua
		END
	ELSE IF(@soluong IS NULL OR @soluong <= 0) 
		BEGIN
		   	--PRINT('So luong phai duong') 
			select cast(-1 as int) as ketqua
		END
	ELSE IF(NOT EXISTS(SELECT*FROM dbo.tbl_datphong WHERE maPhong IN(@maPhong) AND ngayTra > CAST(GETDATE() AS date) AND trangthai='dat'))
		BEGIN
		    --PRINT('Ma phong khong hop le(phong trong/khong ton tai,...)')
			select cast(0 as int) as ketqua
		END
	ELSE IF(EXISTS(SELECT*FROM dbo.tbl_dangkyDoUong WHERE ngay=@ngay AND maPhong=@maPhong AND maDU=@maDoUong))
		BEGIN
		    UPDATE dbo.tbl_dangkyDoUong SET soLuong=soLuong+@soluong WHERE ngay=@ngay AND maPhong=@maPhong AND maDU=@maDoUong
			--PRINT('Dat do uong thanh cong')
			select cast(1 as int) as ketqua
		END
	ELSE
		BEGIN
			INSERT INTO dbo.tbl_dangkyDoUong(ngay,maPhong,maDU,soLuong)VALUES
			(@ngay,@maPhong,@maDoUong,@soluong);
			--PRINT('Dat do uong thanh cong')
			select cast(1 as int) as ketqua
		END
END
GO
-- Ví dụ về đặt đồ uống
EXEC dbo.datDoUong @maPhong = 'a1', @maDoUong = 'cep', @soluong = 3
GO
EXEC dbo.datDoUong @maPhong = 'a1', @maDoUong = 'nl', @soluong = 3
GO
EXEC dbo.datDoUong @maPhong = 'a1', @maDoUong = 'stbo', @soluong = 3
GO
EXEC dbo.datDoUong @maPhong = 'a1', @maDoUong = 'camv', @soluong = 3
GO
EXEC dbo.datDoUong @maPhong = 'a3', @maDoUong = 'cep', @soluong = 3
GO
EXEC dbo.datDoUong @maPhong = 'a3', @maDoUong = 'nl', @soluong = 3
GO
EXEC dbo.datDoUong @maPhong = 'a3', @maDoUong = 'stbo', @soluong = 3
GO
EXEC dbo.datDoUong @maPhong = 'a3', @maDoUong = 'camv', @soluong = 3
GO
EXEC dbo.datDoUong @maPhong = 'b1', @maDoUong = 'cep', @soluong = 3
GO
EXEC dbo.datDoUong @maPhong = 'b1', @maDoUong = 'nl', @soluong = 3
GO
EXEC dbo.datDoUong @maPhong = 'b1', @maDoUong = 'stbo', @soluong = 3
GO
EXEC dbo.datDoUong @maPhong = 'b1', @maDoUong = 'camv', @soluong = 3
GO
SELECT * FROM dbo.tbl_dangkyDoUong
GO
-- proc thanh toan tien (dat thanhtoan=1) --> thanh toán cho 1 nguoi / 1 phong
CREATE PROC thanhToan(@maKH VARCHAR(30), @maPhong VARCHAR(30), @ngayDat DATE)
AS
BEGIN
	IF(NOT EXISTS(SELECT*FROM dbo.tbl_datphong WHERE maKH IN(@maKH) AND maPhong IN(@maPhong) AND ngayDat=@ngayDat AND CAST(GETDATE() AS DATE) >= ngayTra))
		BEGIN
		    SELECT cast(0 as int) as ketqua
		END
    ELSE
		BEGIN
		    UPDATE dbo.tbl_datphong SET thanhtoan=1 --, ngayTra=CAST(GETDATE() AS date)
			WHERE maKH=@maKH AND maPhong=@maPhong AND ngayDat=@ngayDat;
			SELECT cast(1 as int) as ketqua
		END
END
GO
-- Ví dụ về thanh toán
EXEC dbo.thanhToan @maKH = 'kh01',@maPhong = 'a1', @ngayDat = '2023-01-02'
GO
SELECT * FROM dbo.tbl_datphong
GO
-- hoa don do an theo phong
CREATE PROC hoaDonDoAn(@maPhong VARCHAR(30), @ngayDat DATE, @ngayTra DATE)
AS
BEGIN
	SELECT a.maPhong, @ngayDat AS [Ngay Dat Phong], @ngayTra AS [Ngay Tra Phong] , SUM(b.gia*a.soLuong) AS [Gia Do An] FROM dbo.tbl_dangkyDoAn a JOIN dbo.tbl_doAn b ON b.maDA = a.maDA
	AND a.maPhong IN(@maPhong) AND a.ngay BETWEEN @ngayDat AND @ngayTra GROUP BY a.maPhong
END
GO
-- Ví dụ
EXEC dbo.hoaDonDoAn @maPhong = 'a1', @ngayDat = '2023-01-02',  @ngayTra = '2023-10-01' 
GO
-- hoa don do uong theo phong
CREATE PROC hoaDonDoUong(@maPhong VARCHAR(30), @ngayDat DATE, @ngayTra DATE)
AS
BEGIN
	SELECT a.maPhong, @ngayDat AS [Ngay Dat Phong], @ngayTra AS [Ngay Tra Phong] , SUM(b.gia*a.soLuong) AS [Gia Do Uong] FROM dbo.tbl_dangkyDoUong a JOIN dbo.tbl_doUong b ON b.maDU = a.maDU
	AND a.maPhong IN(@maPhong) AND a.ngay BETWEEN @ngayDat AND @ngayTra GROUP BY a.maPhong
END
GO
-- Ví dụ
EXEC dbo.hoaDonDoUong @maPhong = 'a1', @ngayDat = '2023-01-02',  @ngayTra = '2023-10-01' 
GO
-- hoa don tien phong(chua tinh do an, do uong(neu co))
CREATE PROC hoaDonPhong(@maPhong VARCHAR(30), @ngayDat DATE, @ngayTra DATE, @thanhtoan bit)
AS
BEGIN
  SELECT a.maPhong, @ngayDat AS [Ngay Dat Phong], @ngayTra AS [Ngay Tra Phong], c.gia*datediff(day,@ngayDat, @ngayTra) AS [Gia Phong] FROM dbo.tbl_datphong a JOIN dbo.tbl_phong b ON b.maPhong = a.maPhong JOIN dbo.tbl_loaiphong c ON c.maLoaiPhong = b.maLoaiPhong
  AND a.maPhong IN(@maPhong) AND a.ngayDat=@ngayDat AND a.ngayTra=@ngayTra AND a.trangthai='dat' AND a.thanhtoan=@thanhtoan
END
GO
-- SELECT DATEDIFF(DAY,'2023-01-02','2023-10-01')
-- Ví dụ
EXEC dbo.hoaDonPhong @maPhong = 'a1', @ngayDat = '2023-01-02',  @ngayTra = '2023-10-01' , @thanhtoan=0
GO
-- hoa don theo phong(tong tat cả chi phi(tien phong, do an, do uong))
-- Thanh toan = 0 la chua thanh toan; thanh toan = 1 -> thanh toan roi -> gia=0
CREATE PROC hoaDonTheoPhong(@maPhong VARCHAR(30), @ngayDat DATE, @ngayTra DATE)
AS
BEGIN
    IF(NOT EXISTS(SELECT*FROM dbo.tbl_datphong WHERE maPhong IN(@maPhong) AND ngayDat=@ngayDat AND @ngayTra=@ngayTra AND trangthai='dat' AND thanhtoan=0))
		BEGIN
		    SELECT 0 AS [Tong Hoa Don]
		END
	ELSE
		BEGIN
			DECLARE @tienPhong MONEY, @tienDoAn MONEY, @tienDoUong MONEY;

			SET @tienPhong=(SELECT c.gia*datediff(day,a.ngayDat, a.ngayTra) AS [Gia Phong] FROM dbo.tbl_datphong a JOIN dbo.tbl_phong b ON b.maPhong = a.maPhong JOIN dbo.tbl_loaiphong c ON c.maLoaiPhong = b.maLoaiPhong
				AND a.maPhong IN(@maPhong) AND (a.ngayDat=@ngayDat AND a.ngayTra=@ngayTra) AND a.trangthai='dat' AND a.thanhtoan=0);
			----------------------------------------------------------------------------------------------------------------------------------
			IF(EXISTS(SELECT*FROM dbo.tbl_dangkyDoAn WHERE maPhong IN(@maPhong) AND (ngay BETWEEN @ngayDat AND @ngayTra)))
				BEGIN
					SET @tienDoAn=(SELECT SUM(b.gia*a.soLuong) AS [Gia Do An] FROM dbo.tbl_dangkyDoAn a JOIN dbo.tbl_doAn b ON b.maDA = a.maDA
						AND a.maPhong IN(@maPhong) AND (a.ngay BETWEEN @ngayDat AND @ngayTra) GROUP BY a.maPhong);	    
				END
			----------------------------------------------------------------------------------------------------------------------------------
			ELSE
				BEGIN
				    SET @tienDoAn=0;
				END
			----------------------------------------------------------------------------------------------------------------------------------
			IF(EXISTS(SELECT*FROM dbo.tbl_dangkyDoUong WHERE maPhong IN(@maPhong) AND (ngay BETWEEN @ngayDat AND @ngayTra)))
				BEGIN
					SET @tienDoUong=(SELECT SUM(b.gia*a.soLuong) AS [Gia Do Uong] FROM dbo.tbl_dangkyDoUong a JOIN dbo.tbl_doUong b ON b.maDU = a.maDU
						AND a.maPhong=@maPhong AND a.ngay BETWEEN @ngayDat AND @ngayTra GROUP BY a.maPhong);
				END
			----------------------------------------------------------------------------------------------------------------------------------
			ELSE
				BEGIN
				    SET @tienDoUong=0;
				END
			-- Hien thi
			--SELECT c.maKH, c.tenKH, b.tenPhong, a.ngayDat AS [Ngay Dat Phong], a.ngayTra AS [Ngay Tra Phong], (@tienPhong+@tienDoAn+@tienDoUong) AS [Tong Hoa Don] FROM dbo.tbl_datphong a 
			--	JOIN dbo.tbl_phong b ON b.maPhong = a.maPhong 
			--	JOIN dbo.tbl_khachhang c ON c.maKH = a.maKH AND a.ngayDat=@ngayDat AND a.ngayTra=@ngayTra
			RETURN (@tienPhong+@tienDoAn+@tienDoUong);
		END
END
GO
-- Ví dụ
EXEC dbo.hoaDonTheoPhong @maPhong = 'a1', @ngayDat = '2023-01-02',  @ngayTra = '2023-10-01'
GO
EXEC dbo.hoaDonTheoPhong @maPhong = 'a3', @ngayDat = '2023-01-02',  @ngayTra = '2023-01-30'
GO
EXEC dbo.hoaDonTheoPhong @maPhong = 'b1', @ngayDat = '2023-01-02',  @ngayTra = '2023-01-20'
GO
-- Hoa don theo khach hang (tong tat ca cac chi phi(tien phong, do an, do uong))
CREATE PROC hoaDonTheoKhachHang(@maKH VARCHAR(30))
AS
BEGIN
	IF(EXISTS(SELECT*FROM dbo.tbl_datphong WHERE maKH IN(@maKH)))
		BEGIN
			DECLARE @cnt INT;
			SET @cnt=1;
			DECLARE @hoaDon MONEY; SET @hoaDon=0;
			WHILE(@cnt <= (SELECT COUNT(*) FROM dbo.tbl_datphong WHERE maKH IN(@maKH) AND trangthai='dat' AND thanhtoan=0))
				BEGIN
				    DECLARE @maPhong VARCHAR(30);
					DECLARE @ngayDat DATE, @ngayTra DATE; DECLARE @temp MONEY;
					SET @maPhong=(SELECT b.maPhong FROM(SELECT ROW_NUMBER() OVER (ORDER BY a.maPhong ASC) AS num, maPhong
							FROM (SELECT maPhong FROM dbo.tbl_datphong WHERE maKH IN(@maKH) AND trangthai='dat' AND thanhtoan=0) AS a)
							AS b WHERE b.num=@cnt)
					-------------------------------------------------------------------------------------------------------
					SET @ngayDat=(SELECT b.ngayDat FROM(SELECT ROW_NUMBER() OVER (ORDER BY a.maPhong ASC) AS num, a.ngayDat
							FROM (SELECT * FROM dbo.tbl_datphong WHERE maKH IN(@maKH) AND trangthai='dat' AND thanhtoan=0) AS a)
							AS b WHERE b.num=@cnt)
					-------------------------------------------------------------------------------------------------------
					SET @ngayTra=(SELECT b.ngayTra FROM(SELECT ROW_NUMBER() OVER (ORDER BY a.maPhong ASC) AS num, a.ngayTra
							FROM (SELECT * FROM dbo.tbl_datphong WHERE maKH IN(@maKH) AND trangthai='dat' AND thanhtoan=0) AS a)
							AS b WHERE b.num=@cnt)
					-------------------------------------------------------------------------------------------------------
					EXEC @temp=dbo.hoaDonTheoPhong @maPhong = @maPhong, @ngayDat = @ngayDat, @ngayTra = @ngayTra
					SET @hoaDon = @hoaDon + @temp;
					SET @cnt = @cnt + 1;
				END
			SELECT maKH, tenKH, @hoaDon AS hoaDon FROM dbo.tbl_khachhang WHERE maKH IN(@maKH)
		END
END
GO
-- Ví dụ
EXEC hoaDonTheoKhachHang @maKH='kh01'
GO
EXEC hoaDonTheoKhachHang @maKH='kh02'
GO
-- Tinh doanh thu cua khach san trong thang=?, nam=? (thanhtoan=1)
CREATE PROC doanhThu(@ngayBD DATE, @ngayKT DATE)
AS
BEGIN
	DECLARE @tienPhong MONEY, @tienDoAn MONEY, @tienDoUong MONEY;
			SET @tienPhong=(SELECT SUM(c.gia*datediff(day,a.ngayDat, a.ngayTra)) AS [Gia Phong] FROM dbo.tbl_datphong a JOIN dbo.tbl_phong b ON b.maPhong = a.maPhong JOIN dbo.tbl_loaiphong c ON c.maLoaiPhong = b.maLoaiPhong
				AND a.ngayDat>=@ngayBD AND a.ngayTra<=@ngayKT AND a.trangthai='dat');
			SET @tienDoAn=(SELECT SUM(b.gia*a.soLuong) AS [Gia Do An] FROM dbo.tbl_dangkyDoAn a JOIN dbo.tbl_doAn b ON b.maDA = a.maDA
				AND a.ngay BETWEEN @ngayBD AND @ngayKT);
			SET @tienDoUong=(SELECT SUM(b.gia*a.soLuong) AS [Gia Do Uong] FROM dbo.tbl_dangkyDoUong a JOIN dbo.tbl_doUong b ON b.maDU = a.maDU
				AND a.ngay BETWEEN @ngayBD AND @ngayKT);
			IF(@tienPhong IS NULL) SET @tienPhong=0;
			IF(@tienDoAn IS NULL) SET @tienDoAn=0;
			IF(@tienDoUong IS NULL) SET @tienDoUong=0;
			-- Hien thi
			SELECT @ngayBD AS tu, @ngayKT AS den, (@tienPhong+@tienDoAn+@tienDoUong) AS doanhThu
END
GO
-- Ví dụ
EXEC dbo.doanhThu @ngayBD = '2023-01-02', @ngayKT = '2023-10-01'
GO
EXEC dbo.doanhThu @ngayBD = '2023-01-02', @ngayKT = '2023-01-02'
GO
EXEC dbo.doanhThu @ngayBD = '2022-01-03', @ngayKT = '2023-01-31'
GO
--------------------------- BAO CAO THONG KE --------------------------------
-- proc thong ke Top 3 khach hang dat/hen nhieu nhat
CREATE PROC khachHangDatPhongMax
AS
BEGIN
    SELECT TOP 3 a.maKH, b.tenKH, b.sdt, COUNT(a.maKH) AS dem
	FROM tbl_datphong a JOIN tbl_khachhang b ON b.maKH = a.maKH GROUP BY a.maKH, b.tenKH, b.sdt ORDER BY COUNT(a.maKH) DESC
END
GO
-- Ví dụ
EXEC khachHangDatPhongMax
GO
-- proc thong ke nhung khach hang qua han thanh toan
CREATE PROC khachHangQH
AS
BEGIN
    SELECT b.maKH, b.tenKH, b.sdt FROM dbo.tbl_datphong a 
	JOIN tbl_khachhang b ON b.maKH = a.maKH AND a.trangthai='dat' AND a.thanhtoan=0 AND a.ngayTra < CAST(GETDATE() AS DATE)
	GROUP BY b.maKH, b.tenKH,b.sdt
END
GO
-- Ví dụ
EXEC dbo.khachHangQH
GO
-- Hien thi ra phong, ngay dat, ngay tra, trangthai , thanhtoan=0
CREATE PROC chiTietKhachHangQH (@maKH VARCHAR(30))
AS
BEGIN
	SELECT b.maPhong AS[Ma Phong],b.tenPhong AS[Ten Phong],a.ngayDat AS[Ngay Dat], a.ngayTra AS[Ngay Tra], a.trangthai AS[Trang Thai], a.thanhtoan AS[Thanh Toan]
	FROM tbl_datphong a JOIN tbl_phong b ON b.maPhong = a.maPhong AND a.maKH IN(@maKH) AND a.trangthai='dat' AND a.thanhtoan=0 AND a.ngayTra < CAST(GETDATE() AS DATE)
END
GO
-- Ví dụ
EXEC chiTietKhachHangQH @maKH='kh01'
GO
-- proc thong ke nhung khach hang dang hen phong
CREATE PROC khachHangHenPhong
AS
BEGIN
    SELECT b.maKH, b.tenKH, b.sdt FROM dbo.tbl_datphong a 
	JOIN tbl_khachhang b ON b.maKH = a.maKH AND a.trangthai='hen'
	GROUP BY b.maKH, b.tenKH,b.sdt
END
GO
-- Ví dụ
EXEC dbo.khachHangHenPhong;
GO
-- Hien thi ra phong, ngay dat, ngay tra
CREATE PROC chiTietKhachHangHenPhong (@maKH VARCHAR(30))
AS
BEGIN
	SELECT b.maPhong AS[Ma Phong],b.tenPhong AS[Ten Phong],a.ngayDat AS[Ngay Dat], a.ngayTra AS[Ngay Tra], a.trangthai AS[Trang Thai], a.thanhtoan AS[Thanh Toan]
	FROM tbl_datphong a JOIN tbl_phong b ON b.maPhong = a.maPhong AND a.maKH IN(@maKH) AND a.trangthai='hen'
END
GO
-- Ví dụ
EXEC chiTietKhachHangHenPhong @maKH='kh01'
GO
-- proc thong ke nhung khach hang dat phong trong thang=?, nam=? --> So luong khach hang
CREATE PROC tkKhachHangDatPhongTheoMY(@thang VARCHAR(10), @nam VARCHAR(100))
AS
BEGIN
    IF(@thang='' OR @thang IS NULL) PRINT('Thang khong duoc trong')
	ELSE IF(@thang NOT LIKE'%[0-9]%') PRINT('Dau vao thang khong dung')
	ELSE IF(CAST(@thang AS INT) NOT BETWEEN 1 AND 12) PRINT('Dau vao thang khong dung')
	ELSE IF(@nam='' OR @nam IS NULL) PRINT('Nam khong duoc trong')
	ELSE IF(@nam NOT LIKE '%[0-9]%') PRINT('Dau vao nam khong dung')
	ELSE
		BEGIN
			-- Hien thi ra khach hang
			SELECT b.maKH AS[Ma Khach Hang], b.tenKH AS[Ten Khach Hang], b.sdt AS[So Dien Thoai] FROM tbl_datphong a JOIN tbl_khachhang b ON b.maKH = a.maKH AND MONTH(a.ngayDat)=@thang AND YEAR(a.ngayDat)=@nam 
				AND a.trangthai='dat' GROUP BY b.maKH, b.tenKH, b.sdt
			-- Hien thi ra tong so luong
			SELECT COUNT(DISTINCT a.maKH) AS[Tong So Khach Hang] FROM tbl_datphong a JOIN tbl_khachhang b ON b.maKH = a.maKH AND MONTH(a.ngayDat)=@thang AND YEAR(a.ngayDat)=@nam 
				AND a.trangthai='dat'
		END
END
GO
-- Ví dụ
EXEC tkKhachHangDatPhongTheoMY @thang='1', @nam='2023'
GO
-- proc thong ke top 3 phong duoc dat nhieu nhat
CREATE PROC phongDatMax
AS
BEGIN
    SELECT TOP 3 a.maPhong, b.tenPhong, COUNT(a.maPhong) AS dem
	FROM tbl_datphong a JOIN tbl_phong b ON b.maPhong = a.maPhong GROUP BY a.maPhong,b.tenPhong ORDER BY COUNT(a.maPhong) DESC
END
GO
-- Ví dụ
EXEC phongDatMax
GO
-- proc thong ke top 3 do an duoc dat nhieu nhat
CREATE PROC doAnDatMax
AS
BEGIN
    SELECT TOP 3 a.maDA, b.tenDA, SUM(a.soLuong) AS soLuong
	FROM tbl_dangkyDoAn a JOIN tbl_doAn b ON b.maDA = a.maDA GROUP BY a.maDA,b.tenDA ORDER BY SUM(a.soLuong) DESC
END
GO
-- Ví dụ
EXEC doAnDatMax
GO
-- proc thong ke top 3 do uong duoc dat nhieu nhat
CREATE PROC doUongDatMax
AS
BEGIN
    SELECT TOP 3 a.maDU, b.tenDU, SUM(a.soLuong) AS soLuong
	FROM tbl_dangkyDoUong a JOIN tbl_doUong b ON b.maDU = a.maDU GROUP BY a.maDU, b.tenDU ORDER BY SUM(a.soLuong) DESC
END
GO
-- Ví dụ
EXEC doUongDatMax

