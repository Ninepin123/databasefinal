-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- 主機： 127.0.0.1
-- 產生時間： 2026-01-07 06:00:09
-- 伺服器版本： 10.4.32-MariaDB
-- PHP 版本： 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 資料庫： `scholarshipsystem`
--

-- --------------------------------------------------------

--
-- 資料表結構 `admins`
--

CREATE TABLE `admins` (
  `UserID` int(11) NOT NULL COMMENT '使用者編號(管理員)',
  `LastLoginTime` datetime DEFAULT NULL COMMENT '最後登入時間'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- 資料表結構 `advisors`
--

CREATE TABLE `advisors` (
  `UserID` int(11) NOT NULL COMMENT '使用者編號(導師)',
  `Office` varchar(50) DEFAULT NULL COMMENT '辦公室',
  `Department` varchar(50) DEFAULT NULL COMMENT '系所',
  `Title` varchar(50) DEFAULT NULL COMMENT '職稱'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- 資料表結構 `announcements`
--

CREATE TABLE `announcements` (
  `AnnouncementID` int(11) NOT NULL COMMENT '公告編號',
  `PublishDate` datetime DEFAULT current_timestamp() COMMENT '發布日期',
  `Content` text DEFAULT NULL COMMENT '內容',
  `Title` varchar(255) DEFAULT NULL COMMENT '標題',
  `AdminID` int(11) DEFAULT NULL COMMENT '使用者編號(管理員)',
  `UpdatedAt` datetime DEFAULT NULL COMMENT '更新日期',
  `IsActive` tinyint(1) DEFAULT 1 COMMENT '是否啟用'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- 資料表結構 `applications`
--

CREATE TABLE `applications` (
  `ApplicationID` int(11) NOT NULL COMMENT '申請資料編號',
  `Status` varchar(20) DEFAULT 'Pending' COMMENT '狀態',
  `SubmitDate` datetime DEFAULT current_timestamp() COMMENT '送出日期',
  `StudentID` int(11) DEFAULT NULL COMMENT '使用者編號(學生)',
  `ScholarshipID` int(11) DEFAULT NULL COMMENT '獎助學金編號'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- 資料表結構 `attachments`
--

CREATE TABLE `attachments` (
  `AttachmentID` int(11) NOT NULL COMMENT '附件編號',
  `Description` varchar(255) DEFAULT NULL COMMENT '檔案說明',
  `FileName` varchar(255) NOT NULL COMMENT '檔案名稱',
  `FileType` varchar(50) DEFAULT NULL COMMENT '檔案類型',
  `UploadDate` datetime DEFAULT current_timestamp() COMMENT '上傳日期',
  `ApplicationID` int(11) DEFAULT NULL COMMENT '申請資料編號'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- 資料表結構 `donate`
--

CREATE TABLE `donate` (
  `ScholarshipID` int(11) NOT NULL COMMENT '獎助學金編號 (FK)',
  `UnitID` int(11) NOT NULL COMMENT '單位編號 (FK)'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- 資料表結構 `historyrecords`
--

CREATE TABLE `historyrecords` (
  `HistoryID` int(11) NOT NULL COMMENT '歷史紀錄編號',
  `ApprovedAmount` decimal(10,2) DEFAULT NULL COMMENT '核定金額',
  `Result` varchar(50) DEFAULT NULL COMMENT '審查結果',
  `Year` int(11) DEFAULT NULL COMMENT '獎助年度',
  `Semester` varchar(20) DEFAULT NULL COMMENT '學期',
  `StudentID` int(11) DEFAULT NULL COMMENT '使用者編號(學生)',
  `ReviewID` int(11) DEFAULT NULL COMMENT '審查編號'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- 資料表結構 `notifications`
--

CREATE TABLE `notifications` (
  `NotificationID` int(11) NOT NULL COMMENT '通知編號',
  `Content` text DEFAULT NULL COMMENT '訊息內容',
  `IsRead` tinyint(1) DEFAULT 0 COMMENT '已讀狀態',
  `Title` varchar(255) DEFAULT NULL COMMENT '標題',
  `ReviewerID` int(11) DEFAULT NULL COMMENT '使用者編號(審查員) - 可能是發送者',
  `StudentID` int(11) DEFAULT NULL COMMENT '使用者編號(學生) - 可能是接收者'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- 資料表結構 `recommendationletters`
--

CREATE TABLE `recommendationletters` (
  `AdvisorID` int(11) NOT NULL COMMENT '使用者編號(導師)',
  `ApplicationID` int(11) NOT NULL COMMENT '申請資料編號',
  `Content` text DEFAULT NULL COMMENT '內容',
  `FillDate` datetime DEFAULT NULL COMMENT '填寫日期'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- 資料表結構 `reviewers`
--

CREATE TABLE `reviewers` (
  `UserID` int(11) NOT NULL COMMENT '使用者編號(審查員)',
  `ReviewUnit` varchar(100) DEFAULT NULL COMMENT '審查單位'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- 資料表結構 `reviewrecords`
--

CREATE TABLE `reviewrecords` (
  `ReviewID` int(11) NOT NULL COMMENT '審查編號',
  `ReviewDate` datetime DEFAULT NULL COMMENT '審查日期',
  `Result` varchar(50) DEFAULT NULL COMMENT '審查結果',
  `ReviewerID` int(11) DEFAULT NULL COMMENT '使用者編號(審查員)',
  `ApplicationID` int(11) DEFAULT NULL COMMENT '申請資料編號'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- 資料表結構 `scholarships`
--

CREATE TABLE `scholarships` (
  `ScholarshipID` int(11) NOT NULL COMMENT '獎助學金編號',
  `Deadline` datetime DEFAULT NULL COMMENT '申請截止日期',
  `StartDate` datetime DEFAULT NULL COMMENT '申請開始日期',
  `Conditions` text DEFAULT NULL COMMENT '申請條件',
  `Name` varchar(255) NOT NULL COMMENT '名稱',
  `Quota` int(11) DEFAULT NULL COMMENT '申請人數限制',
  `Description` text DEFAULT NULL COMMENT '說明',
  `Type` varchar(50) DEFAULT NULL COMMENT '類型',
  `Amount` decimal(10,2) DEFAULT NULL COMMENT '金額',
  `UnitID` int(11) DEFAULT NULL COMMENT '單位編號'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- 資料表結構 `sponsorunits`
--

CREATE TABLE `sponsorunits` (
  `UnitID` int(11) NOT NULL COMMENT '單位編號',
  `ContactPerson` varchar(50) DEFAULT NULL COMMENT '聯絡人',
  `UnitName` varchar(100) NOT NULL COMMENT '單位名稱',
  `Email` varchar(100) DEFAULT NULL COMMENT '電子郵件',
  `Address` varchar(255) DEFAULT NULL COMMENT '地址',
  `Website` varchar(255) DEFAULT NULL COMMENT '網站',
  `Phone` varchar(20) DEFAULT NULL COMMENT '電話',
  `AdminID` int(11) DEFAULT NULL COMMENT '使用者編號(管理員)'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- 資料表結構 `studentrecords`
--

CREATE TABLE `studentrecords` (
  `StudentID` int(11) NOT NULL COMMENT '使用者編號(學生)',
  `Semester` varchar(20) NOT NULL COMMENT '學期',
  `ConductScore` decimal(5,2) DEFAULT NULL COMMENT '操行成績',
  `AttendanceRate` decimal(5,2) DEFAULT NULL COMMENT '出勤率',
  `AverageScore` decimal(5,2) DEFAULT NULL COMMENT '平均成績'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- 資料表結構 `students`
--

CREATE TABLE `students` (
  `UserID` int(11) NOT NULL COMMENT '使用者編號(學生)',
  `Department` varchar(50) DEFAULT NULL COMMENT '系所',
  `Grade` varchar(10) DEFAULT NULL COMMENT '年級',
  `AdvisorID` int(11) DEFAULT NULL COMMENT '使用者編號(導師)'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- 資料表結構 `users`
--

CREATE TABLE `users` (
  `UserID` int(11) NOT NULL COMMENT '使用者編號',
  `Account` varchar(50) NOT NULL COMMENT '帳號',
  `Password` varchar(255) NOT NULL COMMENT '密碼',
  `Name` varchar(50) NOT NULL COMMENT '姓名',
  `Email` varchar(100) NOT NULL COMMENT '電子郵件',
  `Address` varchar(255) DEFAULT NULL COMMENT '地址',
  `Phone` varchar(20) DEFAULT NULL COMMENT '電話'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- 已傾印資料表的索引
--

--
-- 資料表索引 `admins`
--
ALTER TABLE `admins`
  ADD PRIMARY KEY (`UserID`);

--
-- 資料表索引 `advisors`
--
ALTER TABLE `advisors`
  ADD PRIMARY KEY (`UserID`);

--
-- 資料表索引 `announcements`
--
ALTER TABLE `announcements`
  ADD PRIMARY KEY (`AnnouncementID`),
  ADD KEY `AdminID` (`AdminID`);

--
-- 資料表索引 `applications`
--
ALTER TABLE `applications`
  ADD PRIMARY KEY (`ApplicationID`),
  ADD KEY `StudentID` (`StudentID`),
  ADD KEY `ScholarshipID` (`ScholarshipID`);

--
-- 資料表索引 `attachments`
--
ALTER TABLE `attachments`
  ADD PRIMARY KEY (`AttachmentID`),
  ADD KEY `ApplicationID` (`ApplicationID`);

--
-- 資料表索引 `donate`
--
ALTER TABLE `donate`
  ADD PRIMARY KEY (`ScholarshipID`,`UnitID`),
  ADD KEY `UnitID` (`UnitID`);

--
-- 資料表索引 `historyrecords`
--
ALTER TABLE `historyrecords`
  ADD PRIMARY KEY (`HistoryID`),
  ADD KEY `StudentID` (`StudentID`),
  ADD KEY `ReviewID` (`ReviewID`);

--
-- 資料表索引 `notifications`
--
ALTER TABLE `notifications`
  ADD PRIMARY KEY (`NotificationID`),
  ADD KEY `ReviewerID` (`ReviewerID`),
  ADD KEY `StudentID` (`StudentID`);

--
-- 資料表索引 `recommendationletters`
--
ALTER TABLE `recommendationletters`
  ADD PRIMARY KEY (`AdvisorID`,`ApplicationID`),
  ADD KEY `ApplicationID` (`ApplicationID`);

--
-- 資料表索引 `reviewers`
--
ALTER TABLE `reviewers`
  ADD PRIMARY KEY (`UserID`);

--
-- 資料表索引 `reviewrecords`
--
ALTER TABLE `reviewrecords`
  ADD PRIMARY KEY (`ReviewID`),
  ADD KEY `ReviewerID` (`ReviewerID`),
  ADD KEY `ApplicationID` (`ApplicationID`);

--
-- 資料表索引 `scholarships`
--
ALTER TABLE `scholarships`
  ADD PRIMARY KEY (`ScholarshipID`),
  ADD KEY `UnitID` (`UnitID`);

--
-- 資料表索引 `sponsorunits`
--
ALTER TABLE `sponsorunits`
  ADD PRIMARY KEY (`UnitID`),
  ADD KEY `AdminID` (`AdminID`);

--
-- 資料表索引 `studentrecords`
--
ALTER TABLE `studentrecords`
  ADD PRIMARY KEY (`StudentID`,`Semester`);

--
-- 資料表索引 `students`
--
ALTER TABLE `students`
  ADD PRIMARY KEY (`UserID`),
  ADD KEY `AdvisorID` (`AdvisorID`);

--
-- 資料表索引 `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`UserID`),
  ADD UNIQUE KEY `Account` (`Account`);

--
-- 在傾印的資料表使用自動遞增(AUTO_INCREMENT)
--

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `announcements`
--
ALTER TABLE `announcements`
  MODIFY `AnnouncementID` int(11) NOT NULL AUTO_INCREMENT COMMENT '公告編號';

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `applications`
--
ALTER TABLE `applications`
  MODIFY `ApplicationID` int(11) NOT NULL AUTO_INCREMENT COMMENT '申請資料編號';

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `attachments`
--
ALTER TABLE `attachments`
  MODIFY `AttachmentID` int(11) NOT NULL AUTO_INCREMENT COMMENT '附件編號';

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `historyrecords`
--
ALTER TABLE `historyrecords`
  MODIFY `HistoryID` int(11) NOT NULL AUTO_INCREMENT COMMENT '歷史紀錄編號';

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `notifications`
--
ALTER TABLE `notifications`
  MODIFY `NotificationID` int(11) NOT NULL AUTO_INCREMENT COMMENT '通知編號';

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `reviewrecords`
--
ALTER TABLE `reviewrecords`
  MODIFY `ReviewID` int(11) NOT NULL AUTO_INCREMENT COMMENT '審查編號';

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `scholarships`
--
ALTER TABLE `scholarships`
  MODIFY `ScholarshipID` int(11) NOT NULL AUTO_INCREMENT COMMENT '獎助學金編號';

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `sponsorunits`
--
ALTER TABLE `sponsorunits`
  MODIFY `UnitID` int(11) NOT NULL AUTO_INCREMENT COMMENT '單位編號';

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `users`
--
ALTER TABLE `users`
  MODIFY `UserID` int(11) NOT NULL AUTO_INCREMENT COMMENT '使用者編號';

--
-- 已傾印資料表的限制式
--

--
-- 資料表的限制式 `admins`
--
ALTER TABLE `admins`
  ADD CONSTRAINT `admins_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`);

--
-- 資料表的限制式 `advisors`
--
ALTER TABLE `advisors`
  ADD CONSTRAINT `advisors_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`);

--
-- 資料表的限制式 `announcements`
--
ALTER TABLE `announcements`
  ADD CONSTRAINT `announcements_ibfk_1` FOREIGN KEY (`AdminID`) REFERENCES `admins` (`UserID`);

--
-- 資料表的限制式 `applications`
--
ALTER TABLE `applications`
  ADD CONSTRAINT `applications_ibfk_1` FOREIGN KEY (`StudentID`) REFERENCES `students` (`UserID`),
  ADD CONSTRAINT `applications_ibfk_2` FOREIGN KEY (`ScholarshipID`) REFERENCES `scholarships` (`ScholarshipID`);

--
-- 資料表的限制式 `attachments`
--
ALTER TABLE `attachments`
  ADD CONSTRAINT `attachments_ibfk_1` FOREIGN KEY (`ApplicationID`) REFERENCES `applications` (`ApplicationID`);

--
-- 資料表的限制式 `donate`
--
ALTER TABLE `donate`
  ADD CONSTRAINT `donate_ibfk_1` FOREIGN KEY (`ScholarshipID`) REFERENCES `scholarships` (`ScholarshipID`) ON DELETE CASCADE,
  ADD CONSTRAINT `donate_ibfk_2` FOREIGN KEY (`UnitID`) REFERENCES `sponsorunits` (`UnitID`) ON DELETE CASCADE;

--
-- 資料表的限制式 `historyrecords`
--
ALTER TABLE `historyrecords`
  ADD CONSTRAINT `historyrecords_ibfk_1` FOREIGN KEY (`StudentID`) REFERENCES `students` (`UserID`),
  ADD CONSTRAINT `historyrecords_ibfk_2` FOREIGN KEY (`ReviewID`) REFERENCES `reviewrecords` (`ReviewID`);

--
-- 資料表的限制式 `notifications`
--
ALTER TABLE `notifications`
  ADD CONSTRAINT `notifications_ibfk_1` FOREIGN KEY (`ReviewerID`) REFERENCES `reviewers` (`UserID`),
  ADD CONSTRAINT `notifications_ibfk_2` FOREIGN KEY (`StudentID`) REFERENCES `students` (`UserID`);

--
-- 資料表的限制式 `recommendationletters`
--
ALTER TABLE `recommendationletters`
  ADD CONSTRAINT `recommendationletters_ibfk_1` FOREIGN KEY (`AdvisorID`) REFERENCES `advisors` (`UserID`),
  ADD CONSTRAINT `recommendationletters_ibfk_2` FOREIGN KEY (`ApplicationID`) REFERENCES `applications` (`ApplicationID`);

--
-- 資料表的限制式 `reviewers`
--
ALTER TABLE `reviewers`
  ADD CONSTRAINT `reviewers_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`);

--
-- 資料表的限制式 `reviewrecords`
--
ALTER TABLE `reviewrecords`
  ADD CONSTRAINT `reviewrecords_ibfk_1` FOREIGN KEY (`ReviewerID`) REFERENCES `reviewers` (`UserID`),
  ADD CONSTRAINT `reviewrecords_ibfk_2` FOREIGN KEY (`ApplicationID`) REFERENCES `applications` (`ApplicationID`);

--
-- 資料表的限制式 `scholarships`
--
ALTER TABLE `scholarships`
  ADD CONSTRAINT `scholarships_ibfk_1` FOREIGN KEY (`UnitID`) REFERENCES `sponsorunits` (`UnitID`);

--
-- 資料表的限制式 `sponsorunits`
--
ALTER TABLE `sponsorunits`
  ADD CONSTRAINT `sponsorunits_ibfk_1` FOREIGN KEY (`AdminID`) REFERENCES `admins` (`UserID`);

--
-- 資料表的限制式 `studentrecords`
--
ALTER TABLE `studentrecords`
  ADD CONSTRAINT `studentrecords_ibfk_1` FOREIGN KEY (`StudentID`) REFERENCES `students` (`UserID`);

--
-- 資料表的限制式 `students`
--
ALTER TABLE `students`
  ADD CONSTRAINT `students_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`),
  ADD CONSTRAINT `students_ibfk_2` FOREIGN KEY (`AdvisorID`) REFERENCES `advisors` (`UserID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

-- Default Data
INSERT INTO sponsorunits (UnitID, UnitName, ContactPerson, Email, Phone, Address) VALUES (1, 'Default Sponsor Unit', 'Admin', 'admin@example.com', '0912345678', 'School Administration');
