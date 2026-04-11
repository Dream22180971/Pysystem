-- 创建数据库
CREATE DATABASE IF NOT EXISTS pharmacy_system
DEFAULT CHARACTER SET utf8mb4
COLLATE utf8mb4_general_ci;

USE pharmacy_system;

-- 1. 用户表 (userinfo)
CREATE TABLE userinfo (
  Id int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  Username varchar(255) NOT NULL COMMENT '登录账号名',
  password varchar(100) NOT NULL COMMENT '密码',
  Nickname varchar(255) NOT NULL COMMENT '昵称',
  Sex varchar(25) NOT NULL COMMENT '性别',
  Age int(11) NOT NULL COMMENT '年龄',
  Phone varchar(25) NOT NULL COMMENT '手机号',
  Address varchar(100) NOT NULL COMMENT '地址',
  P_id int(11) NOT NULL COMMENT '角色ID',
  Status int(11) NOT NULL COMMENT '状态 (1启用 0禁用)',
  Create_time date DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (Id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 2. 药品表 (drugs)
CREATE TABLE drugs (
  Id int(11) NOT NULL AUTO_INCREMENT COMMENT '药品ID',
  Drugs_name varchar(255) NOT NULL COMMENT '药品名称',
  Nums int(11) NOT NULL COMMENT '药品编号',
  DrugsImage varchar(255) NOT NULL COMMENT '药品图片',
  Category_id int(11) NOT NULL COMMENT '药品分类ID',
  Price double NOT NULL COMMENT '价格',
  People varchar(25) NOT NULL COMMENT '适用人群',
  Use_method varchar(25) NOT NULL COMMENT '使用方法',
  Cid int(11) NOT NULL COMMENT '柜台ID',
  Rid int(11) NOT NULL COMMENT '仓库ID',
  Product_time varchar(100) NOT NULL COMMENT '生产日期',
  Save_time int(11) NOT NULL COMMENT '保质期(月)',
  Status int(11) NOT NULL COMMENT '状态 (1上架 0下架)',
  PRIMARY KEY (Id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='药品表';

-- 3. 分类表 (category)
CREATE TABLE category (
  Category_id int(11) NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  Category_name varchar(100) NOT NULL COMMENT '药品分类名称',
  Status int(11) NOT NULL COMMENT '状态 (1可用 0不可用)',
  PRIMARY KEY (Category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='药品分类表';

-- 4. 角色表 (part)
CREATE TABLE part (
  P_id int(11) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  P_name varchar(100) NOT NULL COMMENT '角色名称 (管理员/员工)',
  Status int(11) NOT NULL COMMENT '状态',
  PRIMARY KEY (P_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 5. 药品采购表 (purchase)
CREATE TABLE purchase (
  Pid int(11) NOT NULL AUTO_INCREMENT COMMENT '采购ID',
  Drugs_name varchar(255) NOT NULL COMMENT '药品名称',
  Num int(11) NOT NULL COMMENT '采购数量',
  Indate date NOT NULL COMMENT '进货时间',
  Rid int(11) NOT NULL COMMENT '仓库ID',
  Marks varchar(255) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (Pid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='药品采购表';

-- 6. 药品销售表 (sale)
CREATE TABLE sale (
  Sale_id int(11) NOT NULL AUTO_INCREMENT COMMENT '销售ID',
  Drugs_name varchar(255) NOT NULL COMMENT '药品名称',
  Price double NOT NULL COMMENT '单价',
  Num int(11) NOT NULL COMMENT '销售数量',
  Total varchar(50) NOT NULL COMMENT '总价',
  Saledate date NOT NULL COMMENT '销售日期',
  Marks varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (Sale_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='药品销售表';

-- 7. 药品库存表 (kcxx)
CREATE TABLE kcxx (
  Kid int(11) NOT NULL AUTO_INCREMENT COMMENT '库存ID',
  Drugs_name varchar(255) NOT NULL COMMENT '药品名称',
  Num int(11) NOT NULL COMMENT '库存数量',
  Rid int(11) NOT NULL COMMENT '仓库ID',
  Marks varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (Kid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='药品库存表';

-- 8. 仓库表 (repertory)
CREATE TABLE repertory (
  Rid int(11) NOT NULL AUTO_INCREMENT COMMENT '仓库ID',
  Place varchar(100) NOT NULL COMMENT '仓库位置',
  Status int(11) NOT NULL COMMENT '状态',
  PRIMARY KEY (Rid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='仓库表';

-- 9. 柜台表 (counter)
CREATE TABLE counter (
  Cid int(11) NOT NULL AUTO_INCREMENT COMMENT '柜台ID',
  Place varchar(100) NOT NULL COMMENT '柜台位置',
  Status int(11) NOT NULL COMMENT '状态',
  PRIMARY KEY (Cid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='柜台表';

-- ==================== 初始化数据 ====================
-- 插入角色
INSERT INTO part (P_id, P_name, Status) VALUES
(1, '管理员', 1),
(2, '员工', 1);

-- 插入管理员账号 (密码使用MD5加密，示例密码为 admin123)
INSERT INTO userinfo (Username, password, Nickname, Sex, Age, Phone, Address, P_id, Status, Create_time) VALUES
('admin', '0192023a7bbd73250516f069df18b500', '系统管理员', '男', 30, '13800000000', '总部', 1, 1, CURDATE());

-- 插入示例员工账号 (密码 employee123)
INSERT INTO userinfo (Username, password, Nickname, Sex, Age, Phone, Address, P_id, Status, Create_time) VALUES
('emp01', '033836b6cedd9a857d82681aafadbc19', '示例员工', '女', 25, '13900000000', '示例地址', 2, 1, CURDATE());

-- 插入药品分类示例
INSERT INTO category (Category_name, Status) VALUES
('感冒用药', 1),
('肠胃用药', 1),
('皮肤用药', 1),
('心血管用药', 1);

-- 插入仓库示例
INSERT INTO repertory (Place, Status) VALUES
('主仓库-1楼', 1),
('主仓库-2楼', 1);

-- 插入柜台示例
INSERT INTO counter (Place, Status) VALUES
('A区1号柜', 1),
('B区2号柜', 1);

-- 插入示例药品
INSERT INTO drugs (Drugs_name, Nums, DrugsImage, Category_id, Price, People, Use_method, Cid, Rid, Product_time, Save_time, Status) VALUES
('感冒灵颗粒', 10001, '/static/img/ganmaoling.jpg', 1, 15.80, '成人', '口服，一次1袋', 1, 1, '2024-01-01', 24, 1),
('健胃消食片', 10002, '/static/img/jianwei.jpg', 2, 12.50, '成人儿童', '咀嚼', 2, 1, '2024-02-01', 18, 1);

-- 插入库存数据 (库存数量示例)
INSERT INTO kcxx (Drugs_name, Num, Rid, Marks) VALUES
('感冒灵颗粒', 200, 1, '充足'),
('健胃消食片', 50, 1, '低于预警线(60)');

-- 插入采购记录示例
INSERT INTO purchase (Drugs_name, Num, Indate, Rid, Marks) VALUES
('感冒灵颗粒', 500, CURDATE(), 1, '批量采购');

-- 插入销售记录示例
INSERT INTO sale (Drugs_name, Price, Num, Total, Saledate, Marks) VALUES
('感冒灵颗粒', 15.80, 10, '158.00', CURDATE(), '零售');