# HUB IEEE IFF Campos Centro

Central de acesso aos serviços do Ramo, implementada em Java 21, Spring Boot 4.1.2 e Thymeleaf.

O HUB concentra acessos e documentação operacional. Apresentação institucional, notícias, atividades e listagens de projetos pertencem ao site principal ou aos serviços específicos.

## Executar

Com Docker:

    docker compose up --build -d

Abra http://localhost:8080. O Compose publica somente em 127.0.0.1. Para encerrar, use docker compose down.

Com JDK 21 ou superior:

    ./mvnw verify
    java -Xms64m -Xmx256m -jar target/hub-0.0.1-SNAPSHOT.jar

O Maven Wrapper baixa o Maven automaticamente. O build exige JDK; o JAR pronto precisa apenas do ambiente de execução Java.

## Acessos

- Nuvem e arquivos: endereço do Nextcloud.
- Repositórios: destino futuro, como GitLab ou similar, definido pela gestão.
- Minha conta: portal LLDAP para perfil e alteração de senha.
- Documentação interna: endereço de uma pasta, wiki ou serviço com autenticação própria.
- Documentação pública: guias de acesso e referências consultadas no HUB.
- Capítulos: somente identificação e informações básicas de diretoria.
- Ajuda: perguntas frequentes de acesso.

O HUB não recebe senhas nem cria contas. Os serviços internos controlam a autenticação e as permissões.

## Configuração

Os três endereços planejados já são os padrões abaixo. Para alterá-los no Compose, copie .env.example para .env e ajuste os valores:

| Variável | Destino |
| --- | --- |
| HUB_INVENTORY_URL | Módulo de patrimônio; vazio abre orientação local |
| HUB_NEXTCLOUD_URL | Nextcloud e arquivos |
| HUB_REPOSITORIES_URL | Serviço de repositórios |
| HUB_ACCOUNT_URL | Autoatendimento da conta local |
| HUB_INTERNAL_DOCS_URL | Documentação interna |
| HUB_WEBSITE_URL | Site institucional |
| HUB_SUPPORT_EMAIL | E-mail de suporte |
| PORT | Porta na execução direta; padrão 8080 |

Na execução direta, forneça as variáveis ao processo; Spring não lê .env automaticamente. Após alterar o ambiente no Compose, execute docker compose up -d.

Os atalhos de início usam /acessar/{slug}: abrem o destino configurado ou, se estiver vazio, a orientação do serviço. Nuvem e conta estão identificados como em uso; repositórios e documentação interna seguem em preparação. Essa identificação é mantida no catálogo e não faz monitoramento automático. Sem endereço definido, o atalho abre a orientação local. Um endereço configurado não equivale a monitoramento da sua disponibilidade.

As rotas /repositorios, /projetos e antigas fichas /projetos/{slug} redirecionam ao destino de repositórios configurado. Enquanto ele estiver vazio, direcionam à página de orientação desse acesso. Não existe listagem de projetos no catálogo ou na busca.

## Conteúdo

- src/main/resources/catalogo.yml: links, documentos públicos e diretorias.
- src/main/resources/content/: guias e regimento de referência em Markdown.
- src/main/resources/templates/: páginas e componentes Thymeleaf.
- src/main/resources/static/assets/brand/: logos originais fornecidos.
- src/main/resources/static/assets/: estilos e scripts locais.
- deploy/cloudflared.yml.example: exemplo do túnel no mesmo sistema do Compose.

Editar o catálogo ou os textos exige recompilar a aplicação. Nunca coloque documentos privados ou credenciais no catálogo público. O regimento e os dados de diretoria precisam de confirmação de vigência. EMBS permanece identificado como em fundação.

## Identidade e temas

Os cinco logos foram copiados sem recolorir, inverter ou redesenhar. A marca geral é o logo azul fornecido pelo usuário. Os capítulos usam bases brancas em ambos os temas para preservar o texto escuro das marcas.

O azul primário é IEEE Blue #00629B. A fonte usa Google Sans quando instalada, a fonte do sistema Apple em dispositivos Apple e alternativas comuns nos demais sistemas. Nenhuma fonte proprietária é distribuída ou buscada externamente.

Painéis planos com cantos de 6 px. Controles: 8 px. Apenas cursores nativos, sem brilho, rastros ou elementos seguindo o ponteiro. A preferência de tema é salva localmente e a opção Sistema acompanha o dispositivo.

Os estilos e scripts têm URLs versionadas pelo conteúdo, evitando arquivos antigos no cache após implantação.

Ver [análise de contraste](docs/CONTRASTE.md). Diretrizes de implementação: [Hallmark](https://github.com/Nutlope/hallmark) e [Impeccable](https://github.com/pbakaus/impeccable), subordinadas às escolhas explícitas do usuário.

## Hospedar no desktop

O Compose limita o HUB a 640 MB e uma CPU, executa com usuário sem privilégios e sistema de arquivos em leitura. O limite é orçamento inicial, não capacidade comprovada do conjunto com Nextcloud.

Com Proxmox, começar com uma única VM Linux. Linux direto também é compatível. Preferir compilar fora do desktop servidor e manter backups independentes dos serviços.

Para Cloudflare Tunnel, use o exemplo em deploy/, preencha UUID e credenciais fora do repositório e aponte hub.ieeeiffcampos.com.br para http://127.0.0.1:8080 no mesmo host. Se cloudflared rodar em outro contêiner, use rede compartilhada e http://hub:8080. O exemplo local não se aplica diretamente a outra VM.

Nenhum DNS, túnel ou serviço externo é criado automaticamente.

## Verificação

    ./mvnw verify

Os testes verificam renderização, busca, filtros, conteúdo escapado, links de identidade, redirecionamento de repositórios, logos e cabeçalhos de segurança.

O endereço /actuator/health informa a saúde do HUB, sem detalhes internos. Não monitora os serviços de destino.

O portal LLDAP usa https://sso.ieeeiffcampos.com.br e o Nextcloud usa https://nuvem.ieeeiffcampos.com.br. Os serviços utilizam a mesma conta via LDAP; o nome do host não configura SSO. A recuperação de senha por e-mail ainda depende de configuração e teste do SMTP. No servidor, a interface web padrão do LLDAP escuta na porta 17170. O HUB permanece independente da implantação desses serviços.

## Módulos e repositórios

O catálogo inclui um acesso ao patrimônio. Configure `HUB_INVENTORY_URL` com o endereço do serviço; enquanto vazio, o Hub exibe orientações locais. O Hub continua público e não recebe senhas. O inventário autentica diretamente no LLDAP e controla as permissões no servidor.

- [Hub IEEE IFF](https://github.com/rafaelfrancapc/ieee-iff-hub)
- [Inventário e autenticação](https://github.com/rafaelfrancapc/ieee-iff-patrimonio)
- [Documentação geral e implantação](https://github.com/rafaelfrancapc/ieee-iff-docs)
