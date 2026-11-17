# Product

<!-- impeccable:product-schema 1 -->

## Platform
web

## Stack
Spring Boot e Java, solicitados pelo usuário. Thymeleaf para páginas renderizadas no servidor; CSS e JavaScript locais, sem servidor de frontend separado. A escolha substitui Astro no plano anterior.

## Users
Usuários dos serviços internos do Ramo Estudantil IEEE IFF Campos Centro. Visitantes consultam documentação pública; participantes consultam os acessos e as informações básicas das diretorias.

## Product Purpose
Concentrar links de acesso a serviços, arquivos, documentação, repositórios e gerenciamento da conta local. O site institucional fica responsável por divulgação, história, apresentação e projetos; o HUB não replica esse conteúdo.

## Operating Context
Hospedagem futura em um desktop ThinkCentre M90p com i5-650 ou desktop FX-4300, inicialmente 8 GB de RAM. Proxmox é uma possibilidade. Cloudflare Tunnel publicará hub.ieeeiffcampos.com.br. Nextcloud é o serviço previsto para arquivos; Authentik ou LLDAP são possibilidades futuras.

## Capabilities and Constraints
Catálogo operacional e documentação de acesso público; links configuráveis para Nextcloud/arquivos, documentação interna, repositórios futuros e conta local em LLDAP ou Authentik; FAQ de acesso e diretoria básica dos capítulos. Não listar projetos, atividades, história, missão, área de atuação ou conteúdos de divulgação. As rotas antigas de projetos redirecionam ao acesso de repositórios. Sem cadastro ou formulário de senha no HUB.

## Brand Commitments
O usuário pediu identidade corporativa IEEE: usar o logo azul geral fornecido, os quatro logos dos capítulos e o azul institucional. Tema claro e escuro, bordas um pouco mais arredondadas, fontes comuns do sistema com Google Sans/San Francisco quando disponíveis localmente. Sem cursores customizados, glow, neon ou emojis. Preservar as cores dos logos e verificar a leitura em ambos os temas. A revisão posterior prevalece sobre o monocromatismo do primeiro piloto.

## Evidence on Hand
Logos originais fornecidos pelo usuário: logo.webp (marca geral), cs.png, pes.png, embs.png e RAS.webp, copiados para static/assets/brand. Regimento fornecido como documento de referência. Diretoria informada na conversa. Não há endereços de serviços reais confirmados; não fabricar disponibilidade.

## Product Principles
Recursos úteis antes de divulgação; permissões pertencem ao serviço que guarda o arquivo; conteúdo incompleto é identificado; custo de operação compatível com a máquina; continuidade entre gestões.
