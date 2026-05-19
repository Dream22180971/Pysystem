-- 审计日志表 + 10 条演示数据（在 pharmacy_system 库中执行）
USE pharmacy_system;
SET NAMES utf8mb4;
SET character_set_client = utf8mb4;
SET character_set_connection = utf8mb4;
SET character_set_results = utf8mb4;

CREATE TABLE IF NOT EXISTS audit_log (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  operator VARCHAR(64) NOT NULL COMMENT '操作人',
  module VARCHAR(64) NOT NULL COMMENT '模块',
  action VARCHAR(64) NOT NULL COMMENT '操作类型',
  detail VARCHAR(512) DEFAULT NULL COMMENT '描述',
  ip VARCHAR(64) DEFAULT NULL COMMENT '来源IP',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作审计日志';

TRUNCATE TABLE audit_log;

INSERT INTO audit_log (operator, module, action, detail, ip) VALUES
('admin', 'auth', 'login', 'login ok', '192.168.1.10'),
('admin', 'drugs', 'query', 'list drugs', '192.168.1.10'),
('emp01', 'kcxx', 'update', 'stock update', '192.168.1.22'),
('emp02', 'sale', 'add', 'add sale record', '192.168.1.23'),
('emp03', 'purchase', 'add', 'add purchase', '192.168.1.24'),
('admin', 'user', 'update', 'update user emp05', '192.168.1.10'),
('emp04', 'category', 'query', 'list category', '192.168.1.25'),
('emp05', 'drugs', 'update', 'update drug price', '192.168.1.26'),
('admin', 'auth', 'login', 'login ok', '192.168.1.10'),
('emp06', 'kcxx', 'query', 'warning list', '192.168.1.27');
