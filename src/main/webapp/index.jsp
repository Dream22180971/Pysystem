<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
    <title>智能药店管理系统 - 登录</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/layui@2.9.8/dist/css/layui.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Microsoft YaHei', 'Segoe UI', sans-serif;
            background: #f3f6fb;
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 20px;
        }

        .login-container {
            width: 100%;
            max-width: 440px;
            background: #ffffff;
            border-radius: 16px;
            padding: 36px 32px;
            box-shadow: 0 12px 28px rgba(17, 24, 39, 0.08);
            border: 1px solid #e6ebf2;
        }

        .logo-area {
            text-align: center;
            margin-bottom: 28px;
        }

        .logo-icon-container {
            display: inline-block;
            margin-bottom: 12px;
        }

        .logo-icon {
            font-size: 54px;
            color: #1a73e8;
        }

        .logo-area h1 {
            font-size: 1.9rem;
            font-weight: 600;
            color: #1f2937;
            margin-bottom: 6px;
            letter-spacing: 0;
        }

        .logo-area p {
            font-size: 0.95rem;
            color: #64748b;
            font-weight: 400;
            line-height: 1.5;
        }

        .login-form {
            width: 100%;
        }

        .form-group {
            margin-bottom: 18px;
            position: relative;
        }

        .form-group label {
            display: block;
            font-size: 14px;
            font-weight: 500;
            color: #334155;
            margin-bottom: 8px;
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .form-group label i {
            color: #1a73e8;
            font-size: 14px;
        }

        .input-with-icon {
            position: relative;
        }

        .input-icon {
            position: absolute;
            left: 14px;
            top: 50%;
            transform: translateY(-50%);
            color: #94a3b8;
            font-size: 16px;
            z-index: 2;
            transition: color 0.2s ease;
        }

        .layui-input {
            width: 100%;
            height: 44px;
            padding: 0 14px 0 40px;
            border: 1px solid #dbe3ef;
            border-radius: 10px;
            font-size: 14px;
            color: #1f2937;
            background: #ffffff;
            transition: border-color 0.2s ease, box-shadow 0.2s ease;
            outline: none;
            box-shadow: none;
            font-weight: 400;
        }

        .layui-input:focus {
            border-color: #1a73e8;
            background: #fff;
            box-shadow:
                0 0 0 4px rgba(26, 115, 232, 0.15),
                inset 0 2px 4px rgba(0, 0, 0, 0.02);
        }

        .layui-input:focus + .input-icon {
            color: #1a73e8;
        }

        .layui-input::placeholder {
            color: #9aa0a6;
            font-size: 15px;
            font-weight: 400;
        }

        .login-btn-container {
            margin-top: 45px;
            position: relative;
        }

        .login-btn {
            width: 100%;
            height: 58px;
            background: linear-gradient(135deg, #1a73e8 0%, #34a853 100%);
            border: none;
            border-radius: 14px;
            color: white;
            font-size: 17px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            letter-spacing: 0.5px;
            box-shadow:
                0 8px 25px rgba(26, 115, 232, 0.3),
                0 4px 8px rgba(52, 168, 83, 0.2);
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 12px;
            position: relative;
            overflow: hidden;
            z-index: 1;
        }

        .login-btn::before {
            content: '';
            position: absolute;
            top: 0;
            left: -100%;
            width: 100%;
            height: 100%;
            background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
            transition: left 0.7s ease;
            z-index: -1;
        }

        .login-btn:hover {
            transform: translateY(-3px);
            box-shadow:
                0 12px 30px rgba(26, 115, 232, 0.4),
                0 6px 12px rgba(52, 168, 83, 0.25);
        }

        .login-btn:hover::before {
            left: 100%;
        }

        .login-btn:active {
            transform: translateY(0);
            box-shadow:
                0 4px 15px rgba(26, 115, 232, 0.3),
                0 2px 4px rgba(52, 168, 83, 0.2);
        }

        .login-btn i {
            font-size: 20px;
            transition: transform 0.3s ease;
        }

        .login-btn:hover i {
            transform: translateX(4px);
        }






        /* 医疗特色元素 */
        .medical-elements {
            position: absolute;
            width: 100%;
            height: 100%;
            top: 0;
            left: 0;
            pointer-events: none;
            z-index: 0;
        }

        .pill-element {
            position: absolute;
            width: 40px;
            height: 20px;
            background: linear-gradient(135deg, #1a73e8, #34a853);
            border-radius: 20px;
            opacity: 0.1;
            animation: float-pill 20s linear infinite;
        }

        .pill-element:nth-child(1) {
            top: 15%;
            left: 10%;
            animation-delay: 0s;
        }

        .pill-element:nth-child(2) {
            top: 25%;
            right: 15%;
            animation-delay: -5s;
        }

        .pill-element:nth-child(3) {
            bottom: 30%;
            left: 20%;
            animation-delay: -10s;
        }

        .pill-element:nth-child(4) {
            bottom: 20%;
            right: 10%;
            animation-delay: -15s;
        }

        @keyframes float-pill {
            0%, 100% { transform: translateY(0) rotate(0deg); opacity: 0.1; }
            50% { transform: translateY(-20px) rotate(180deg); opacity: 0.2; }
        }

        /* 移除layui默认样式覆盖 */
        .layui-form-item {
            margin-bottom: 0;
        }
        .layui-form-label {
            display: none;
        }
        .layui-input-block {
            margin-left: 0;
        }

        /* 响应式调整 */
        @media (max-width: 520px) {
            .login-container {
                padding: 40px 30px;
                border-radius: 24px;
                max-width: 100%;
            }

            .logo-area h1 {
                font-size: 2rem;
            }

            .logo-icon {
                font-size: 60px;
            }

            .logo-icon-bg {
                width: 85px;
                height: 85px;
            }

            .layui-input {
                height: 54px;
                padding: 0 18px 0 56px;
            }

            .login-btn {
                height: 54px;
                font-size: 16px;
            }
        }

        @media (max-width: 380px) {
            .login-container {
                padding: 35px 25px;
            }

            .logo-area h1 {
                font-size: 1.8rem;
            }

            .logo-icon {
                font-size: 54px;
            }
        }

        /* 错误提示样式 */
        .error-message {
            color: #ea4335;
            font-size: 13px;
            margin-top: 8px;
            padding-left: 6px;
            display: none;
            animation: fadeIn 0.3s ease;
            font-weight: 500;
        }

        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(-5px); }
            to { opacity: 1; transform: translateY(0); }
        }

        /* 加载状态 */
        .login-btn.loading {
            pointer-events: none;
            opacity: 0.9;
        }

        .login-btn.loading i {
            animation: spin 1s linear infinite;
        }

        @keyframes spin {
            from { transform: rotate(0deg); }
            to { transform: rotate(360deg); }
        }

        /* 安全提示 */
        .security-note {
            font-size: 12px;
            color: #5f6368;
            text-align: center;
            margin-top: 15px;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 6px;
        }

        .security-note i {
            color: #34a853;
        }
    </style>
</head>
<body>
    <div class="medical-elements">
        <div class="pill-element"></div>
        <div class="pill-element"></div>
        <div class="pill-element"></div>
        <div class="pill-element"></div>
    </div>

    <div class="login-container">
        <div class="logo-area">
            <div class="logo-icon-container">
                <div class="logo-icon-bg"></div>
                <div class="logo-icon">
                    <i class="fas fa-capsules"></i>
                </div>
            </div>
            <h1>智慧药房管理系统</h1>
            <p>专业药店管理 · 智能库存监控 · 精准销售分析</p>
        </div>

        <form class="layui-form login-form" id="loginForm">
            <div class="form-group">
                <label for="username">
                    <i class="fas fa-user-md"></i>用户名
                </label>
                <div class="input-with-icon">
                    <i class="fas fa-user input-icon"></i>
                    <input type="text" name="username" id="username" required lay-verify="required"
                           placeholder="请输入管理员账号" autocomplete="username" class="layui-input">
                </div>
                <div class="error-message" id="usernameError"></div>
            </div>

            <div class="form-group">
                <label for="password">
                    <i class="fas fa-shield-alt"></i>密码
                </label>
                <div class="input-with-icon">
                    <i class="fas fa-lock input-icon"></i>
                    <input type="password" name="password" id="password" required lay-verify="required"
                           placeholder="请输入登录密码" autocomplete="current-password" class="layui-input">
                </div>
                <div class="error-message" id="passwordError"></div>
            </div>

            <div class="security-note">
                <i class="fas fa-lock"></i>
                <span>您的登录信息已加密传输，确保安全</span>
            </div>

            <div class="login-btn-container">
                <button class="login-btn" lay-submit lay-filter="login" id="loginButton">
                    <i class="fas fa-sign-in-alt"></i>
                    登录系统
                </button>
            </div>
        </form>

    </div>

    <script src="https://cdn.jsdelivr.net/npm/layui@2.9.8/dist/layui.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.6.0/dist/jquery.min.js"></script>
    <script>
        // 页面加载完成后执行
        $(document).ready(function() {
            // 删除包含默认管理员账号信息的div
            $('.login-footer').remove();
            // 删除包含Powered by信息的div
            $('.system-info').remove();
        });

        layui.use(['form', 'layer'], function() {
            var form = layui.form;
            var layer = layui.layer;

            form.on('submit(login)', function(data) {
                $.ajax({
                    url: '/api/auth/login',
                    type: 'POST',
                    data: data.field,
                    contentType: 'application/x-www-form-urlencoded;charset=utf-8',
                    dataType: 'json',
                    success: function(res) {
                        if (res.code === 200) {
                            layer.msg(res.message, {icon: 1}, function() {
                                window.location.href = '/pages/Home/home.html';
                            });
                        } else {
                            layer.msg(res.message, {icon: 5});
                        }
                    },
                    error: function() {
                        layer.msg('网络错误，请稍后重试', {icon: 5});
                    }
                });
                return false;
            });
        });
    </script>
</body>
</html>