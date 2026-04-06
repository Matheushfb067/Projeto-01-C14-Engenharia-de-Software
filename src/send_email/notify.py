import os
import sys
import smtplib
from email.mime.text import MIMEText

# 1. Captura as variáveis das Secrets do GitHub
dest = os.getenv("EMAIL_TO")
user = os.getenv("SMTP_USER")
raw_password = os.getenv("SMTP_PASSWORD")
password = raw_password.replace(" ", "") if raw_password else None

# Validação das variáveis de ambiente para evitar falhas silenciosas
if not all([dest, user, password]):
    print("Erro: As variáveis de ambiente EMAIL_TO, SMTP_USER ou SMTP_PASSWORD não foram configuradas.")
    sys.exit(1)

# 2. Captura o status do pipeline enviado pelo GitHub Actions
# O Membro 2 vai passar 'success' ou 'failure' como argumento
status = sys.argv[1].lower() if len(sys.argv) > 1 else "finalizado"

# 3. Personaliza a mensagem baseada no status
emoji = "✅" if status == "success" else "❌"
subject = f"{emoji} Status do Pipeline: {status.upper()}"
body = f"O pipeline do Sistema de Filmes foi executado.\nResultado: {status.upper()}"

msg = MIMEText(body)
msg["Subject"] = subject
msg["From"] = user
msg["To"] = dest

# 4. Envio seguro via TLS
try:
    with smtplib.SMTP("smtp.gmail.com", 587) as server:
        server.starttls()
        server.login(user, password)
        server.sendmail(user, [dest], msg.as_string())
    print(f"Notificação de {status} enviada com sucesso para {dest}")
except Exception as e:
    print(f"Erro ao enviar e-mail: {e}")
    sys.exit(1)
