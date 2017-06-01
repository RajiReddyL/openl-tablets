UPDATE UserGroup
SET UserPrivileges = CONCAT(UserPrivileges, ',PRIVILEGE_UNLOCK_PROJECTS,PRIVILEGE_UNLOCK_DEPLOYMENT')
WHERE UserPrivileges LIKE '%PRIVILEGE_ADMINISTRATE%';