## Atualização em 17/11/2026

O Hub agora inclui o acesso configurável ao inventário por `HUB_INVENTORY_URL`. Os **28 testes** passaram, incluindo redirecionamento ao destino configurado e manutenção da autenticação fora do catálogo público. O módulo de patrimônio e a documentação geral são repositórios independentes.

As observações anteriores estão preservadas abaixo como registro histórico.

# Atualização do diretório e serviços

Nuvem e conta passaram a constar como em uso; em 14 de novembro de 2026 seus endereços públicos responderam, o endpoint de status identificou o Nextcloud instalado e a página do LLDAP carregou. A verificação foi pontual e não é monitoramento contínuo. A recuperação por e-mail depende de SMTP. Os 27 testes passaram no build Java 21. Layout e menu foram conferidos nos dois temas e em tela móvel de 390 px.

As capturas em docs/preview e as anotações seguintes correspondem a revisões anteriores.

# Atualização de 14 de novembro de 2026

Os endereços de Nextcloud, GitLab e LLDAP agora têm valores padrão planejados. A versão atual foi compilada com JDK 21 e passou em 27 testes. O Docker estava parado; a prévia foi iniciada diretamente pelo JAR. As observações abaixo registram a revisão anterior.

# Verificação da revisão do HUB

Verificação local concluída em 14 de novembro de 2026.

## Aplicação e conteúdo

- Java 21, Spring Boot 4.1.2 e Thymeleaf. Imagem Docker construída com os testes completos.
- 26 casos de teste: renderização de páginas e fichas, busca com acentos, filtros, estado vazio, 404, HTML escapado, validação de destinos, cabeçalhos, estilos versionados, cinco logos e redirecionamentos de serviços configurados ou ainda sem destino.
- Removidos do catálogo, da busca e da navegação: propaganda, apresentação institucional, histórico e listagem de projetos. As antigas rotas de projetos e /repositorios levam ao serviço de repositórios configurado ou à orientação de implantação.
- FAQ de acesso preservado. Diretorias mostram somente identificação, responsáveis informados e acessos de trabalho.
- Os cinco arquivos de marca foram comparados por SHA-256 e são idênticos aos originais fornecidos.

## Navegador e identidade

- Página inicial conferida nos temas claro e escuro. Logos carregados e preservados em ambos; marcas dos capítulos sobre bases brancas constantes.
- Layout conferido em 1440, 390 e 320 pixels. Sem rolagem horizontal nas larguras móveis inspecionadas. Menu compacto, acesso à conta, FAQ e destino de repositórios verificados no aplicativo em execução.
- Conta e repositórios sem endereço apresentam orientação e situação de preparação; nenhum formulário de senha é simulado.
- Fonte comum do sistema: Google Sans quando instalada, fonte Apple nos dispositivos compatíveis e alternativas Segoe UI/Arial. Nenhuma fonte externa é carregada.
- Cursor computado do corpo: auto. Não existem rastros, elementos seguindo o ponteiro, glow ou cursores por imagem. Controles conservam os cursores nativos e foco visível.
- CSS e JavaScript têm endereços versionados pelo conteúdo. A escolha de tema persiste localmente; a opção Sistema segue a preferência do dispositivo.
- Contraste documentado em CONTRASTE.md e CONTRASTE.json. As razões calculadas para texto principal, secundário, links e botão são adequadas nos pares avaliados. Partes claras das marcas originais não são apresentadas como uma aprovação geral de contraste.

## Revisões de design

- Hallmark: oito páginas HTML já renderizadas receberam análise estática com 0 FAIL e 35 WARN. Os avisos repetidos cobrem cor herdada de superfícies (conferida pelos tokens e no navegador), repetição consistente de painéis corporativos e caret nativo. Não houve REFLEX ou waiver rejeitada no resultado final. A etapa automática de renderização do Hallmark não foi usada; a inspeção visual ocorreu no navegador disponível.
- Impeccable: detector executado uma vez nesta revisão. Dois avisos de hierarquia vieram da análise de templates sem resolver o fragmento de estilos e trataram títulos como 16 px. No aplicativo o corpo tem 16 px, h1 32 px e 28 px no móvel; títulos de seção e painéis usam níveis próprios.
- Revisão independente dos screenshots atuais: alinhamento ao escopo operacional, tipografia, logos, paleta e composição confirmados. Um texto residual dizia que a busca encontrava projetos; corrigido e verificado pelo mesmo revisor. Sua disposição final ship cobre esse ajuste, com pendências declaradas como resolvidas.

## Execução

O Compose mantém a porta somente em 127.0.0.1:8080, limite de 640 MB, uma CPU, usuário sem privilégios e sistema de arquivos em leitura. Esses limites são um orçamento inicial; não constituem teste de capacidade do desktop com Nextcloud e outros serviços.

Nenhum DNS, túnel público, serviço de arquivos ou provedor de identidade foi implantado. Nextcloud, conta local, documentação interna e repositórios dependem dos endereços reais configurados no ambiente. As imagens em docs/preview são capturas do aplicativo local.
