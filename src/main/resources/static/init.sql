--CREATE USER C##user_test IDENTIFIED BY "Secure@Pwa9";
--GRANT CONNECT TO C##user_test;
--GRANT ALL PRIVILEGES TO C##user_test;

CREATE USER C##test_user IDENTIFIED BY "Secure@Pwa9";

-- Grant the CONNECT role to allow the user to connect to the database
GRANT CONNECT TO C##test_user;

-- Optionally, grant additional privileges if needed
-- For example, to grant all privileges (use with caution)
GRANT ALL PRIVILEGES TO C##test_user;