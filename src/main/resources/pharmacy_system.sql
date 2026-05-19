-- 创建数据库
CREATE DATABASE IF NOT EXISTS pharmacy_system
DEFAULT CHARACTER SET utf8mb4
COLLATE utf8mb4_general_ci;

USE pharmacy_system;
SET NAMES utf8mb4;
SET character_set_client = utf8mb4;
SET character_set_connection = utf8mb4;
SET character_set_results = utf8mb4;

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
-- 角色（管理员 / 员工）
INSERT INTO part (P_id, P_name, Status) VALUES
(1, '管理员', 1),
(2, '员工', 1);

-- 用户 10 条：admin 密码 admin123；emp01~emp09 密码均为 employee123（MD5）
INSERT INTO userinfo (Username, password, Nickname, Sex, Age, Phone, Address, P_id, Status, Create_time) VALUES
('admin', '0192023a7bbd73250516f069df18b500', '系统管理员', '男', 30, '13800000001', '总部', 1, 1, CURDATE()),
('emp01', '033836b6cedd9a857d82681aafadbc19', '张小敏', '女', 25, '13900000001', '徐州鼓楼区', 2, 1, CURDATE()),
('emp02', '033836b6cedd9a857d82681aafadbc19', '李强', '男', 28, '13900000002', '徐州云龙区', 2, 1, CURDATE()),
('emp03', '033836b6cedd9a857d82681aafadbc19', '王芳', '女', 32, '13900000003', '徐州泉山区', 2, 1, CURDATE()),
('emp04', '033836b6cedd9a857d82681aafadbc19', '赵磊', '男', 24, '13900000004', '徐州铜山区', 2, 1, CURDATE()),
('emp05', '033836b6cedd9a857d82681aafadbc19', '刘洋', '男', 29, '13900000005', '徐州贾汪区', 2, 1, CURDATE()),
('emp06', '033836b6cedd9a857d82681aafadbc19', '陈静', '女', 27, '13900000006', '徐州经开区', 2, 1, CURDATE()),
('emp07', '033836b6cedd9a857d82681aafadbc19', '周杰', '男', 31, '13900000007', '徐州新城区', 2, 1, CURDATE()),
('emp08', '033836b6cedd9a857d82681aafadbc19', '吴丽', '女', 26, '13900000008', '徐州高新区', 2, 1, CURDATE()),
('emp09', '033836b6cedd9a857d82681aafadbc19', '郑凯', '男', 33, '13900000009', '徐州港区', 2, 1, CURDATE());

-- 药品分类 10 条
INSERT INTO category (Category_name, Status) VALUES
('感冒用药', 1),
('肠胃用药', 1),
('皮肤用药', 1),
('心血管用药', 1),
('儿科用药', 1),
('妇科用药', 1),
('中药饮片', 1),
('医疗器械', 1),
('保健品', 1),
('其他', 1);

-- 仓库 / 柜台
INSERT INTO repertory (Place, Status) VALUES
('主仓库-1楼', 1),
('主仓库-2楼', 1);

INSERT INTO counter (Place, Status) VALUES
('A区1号柜', 1),
('B区2号柜', 1);

-- 药品 10 条（分类 1~10 各一条示例）
INSERT INTO drugs (Drugs_name, Nums, DrugsImage, Category_id, Price, People, Use_method, Cid, Rid, Product_time, Save_time, Status) VALUES
('感冒灵颗粒', 10001, '/static/img/d1.jpg', 1, 15.80, '成人', '口服', 1, 1, '2024-01-01', 24, 1),
('健胃消食片', 10002, '/static/img/d2.jpg', 2, 12.50, '成人儿童', '咀嚼', 2, 1, '2024-02-01', 18, 1),
('阿莫西林胶囊', 10003, '/static/img/d3.jpg', 3, 22.00, '成人', '口服', 1, 1, '2024-03-10', 24, 1),
('布洛芬缓释片', 10004, '/static/img/d4.jpg', 4, 18.60, '成人', '口服', 2, 2, '2024-04-05', 36, 1),
('维生素C片', 10005, '/static/img/d5.jpg', 5, 9.90, '儿童', '口服', 1, 1, '2024-05-12', 24, 1),
('板蓝根颗粒', 10006, '/static/img/d6.jpg', 7, 11.20, '成人', '冲服', 2, 1, '2024-06-01', 18, 1),
('医用口罩', 10007, '/static/img/d7.jpg', 8, 15.00, '通用', '外用', 1, 2, '2024-07-20', 36, 1),
('酒精消毒液', 10008, '/static/img/d8.jpg', 8, 8.50, '通用', '外用', 2, 1, '2024-08-15', 24, 1),
('钙片', 10009, '/static/img/d9.jpg', 9, 45.00, '成人', '口服', 1, 2, '2024-09-01', 24, 1),
('退热贴', 10010, '/static/img/d10.jpg', 5, 19.80, '儿童', '外用', 2, 1, '2024-10-01', 24, 1);

-- 库存 10 条（与上药品名称对应）
INSERT INTO kcxx (Drugs_name, Num, Rid, Marks) VALUES
('感冒灵颗粒', 200, 1, '充足'),
('健胃消食片', 45, 1, '低于预警线(60)'),
('阿莫西林胶囊', 120, 1, '正常'),
('布洛芬缓释片', 88, 2, '正常'),
('维生素C片', 300, 1, '充足'),
('板蓝根颗粒', 55, 1, '预警'),
('医用口罩', 500, 2, '充足'),
('酒精消毒液', 180, 1, '正常'),
('钙片', 90, 2, '正常'),
('退热贴', 40, 1, '预警');

-- 采购 10 条
INSERT INTO purchase (Drugs_name, Num, Indate, Rid, Marks) VALUES
('感冒灵颗粒', 500, DATE_SUB(CURDATE(), INTERVAL 9 DAY), 1, '补货'),
('健胃消食片', 300, DATE_SUB(CURDATE(), INTERVAL 8 DAY), 1, '补货'),
('阿莫西林胶囊', 200, DATE_SUB(CURDATE(), INTERVAL 7 DAY), 1, '集采'),
('布洛芬缓释片', 150, DATE_SUB(CURDATE(), INTERVAL 6 DAY), 2, '补货'),
('维生素C片', 400, DATE_SUB(CURDATE(), INTERVAL 5 DAY), 1, '促销备货'),
('板蓝根颗粒', 350, DATE_SUB(CURDATE(), INTERVAL 4 DAY), 1, '季节性'),
('医用口罩', 800, DATE_SUB(CURDATE(), INTERVAL 3 DAY), 2, '大宗'),
('酒精消毒液', 600, DATE_SUB(CURDATE(), INTERVAL 2 DAY), 1, '大宗'),
('钙片', 120, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 2, '常规'),
('退热贴', 200, CURDATE(), 1, '常规');

-- 销售 10 条（总价 = 单价 * 数量）
INSERT INTO sale (Drugs_name, Price, Num, Total, Saledate, Marks) VALUES
('感冒灵颗粒', 15.80, 10, '158.00', DATE_SUB(CURDATE(), INTERVAL 9 DAY), '零售'),
('健胃消食片', 12.50, 5, '62.50', DATE_SUB(CURDATE(), INTERVAL 8 DAY), '零售'),
('阿莫西林胶囊', 22.00, 8, '176.00', DATE_SUB(CURDATE(), INTERVAL 7 DAY), '零售'),
('布洛芬缓释片', 18.60, 6, '111.60', DATE_SUB(CURDATE(), INTERVAL 6 DAY), '零售'),
('维生素C片', 9.90, 20, '198.00', DATE_SUB(CURDATE(), INTERVAL 5 DAY), '团购'),
('板蓝根颗粒', 11.20, 15, '168.00', DATE_SUB(CURDATE(), INTERVAL 4 DAY), '零售'),
('医用口罩', 15.00, 30, '450.00', DATE_SUB(CURDATE(), INTERVAL 3 DAY), '批发'),
('酒精消毒液', 8.50, 12, '102.00', DATE_SUB(CURDATE(), INTERVAL 2 DAY), '零售'),
('钙片', 45.00, 4, '180.00', DATE_SUB(CURDATE(), INTERVAL 1 DAY), '零售'),
('退热贴', 19.80, 8, '158.40', CURDATE(), '零售');
