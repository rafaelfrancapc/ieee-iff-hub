package br.com.ieeeiffcampos.hub.web;

import br.com.ieeeiffcampos.hub.config.HubProperties;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalPageModel {
    private final HubProperties hub;
    public GlobalPageModel(HubProperties hub) { this.hub = hub; }
    @ModelAttribute("hub")
    HubProperties hub() { return hub; }
    @ModelAttribute("activePage")
    String activePage() { return ""; }
}
