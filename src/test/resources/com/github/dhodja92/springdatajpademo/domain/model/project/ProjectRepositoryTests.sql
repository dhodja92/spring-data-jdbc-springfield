INSERT INTO project (id, name, color)
VALUES ('8008d6ce-9bf9-4c03-8b32-e89f11d6a518', 'NutriU', '#3366ff');
INSERT INTO project (id, name, color)
VALUES ('92cdc2eb-7773-4265-a036-64130e05c0a6', 'Signify', '#00cc66');
INSERT INTO project (id, name, color)
VALUES ('c29bd274-1b07-4d43-9539-e67f54792627', 'Porsche Digital Croatia', '#b32d00');

INSERT INTO task (id, name, label, finished, priority, project_id)
VALUES ('0618899c-6446-4c9d-9307-b8c9ca821b98', 'Resolve DB deadlock bug', 'BUG', FALSE, 'HIGH',
        '92cdc2eb-7773-4265-a036-64130e05c0a6');
INSERT INTO task (id, name, label, finished, priority, project_id)
VALUES ('cf970dae-3552-43d5-b2b9-2dc8e05c8559', 'Estimate user story', 'ESTIMATE', FALSE, 'MEDIUM',
        '8008d6ce-9bf9-4c03-8b32-e89f11d6a518');
INSERT INTO task (id, name, label, finished, priority, project_id)
VALUES ('1ab29750-e9b5-4d80-87ee-d13726bc03f8', 'Refactor payment gateway module', 'TASK', FALSE, 'LOW',
        'c29bd274-1b07-4d43-9539-e67f54792627');
