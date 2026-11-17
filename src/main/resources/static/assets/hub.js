(() => {
 'use strict';
 const root = document.documentElement;
 const picker = document.querySelector('[data-theme-picker]');
 const choices = [...document.querySelectorAll('[data-theme-value]')];
 function applyTheme(value) {
  const preference = ['light','dark','system'].includes(value) ? value : 'system';
  root.dataset.themePreference = preference;
  if (preference === 'system') delete root.dataset.mode;
  else root.dataset.mode = preference;
  choices.forEach(button => button.setAttribute('aria-pressed',String(button.dataset.themeValue === preference)));
 }
 applyTheme(root.dataset.themePreference);
 choices.forEach(button => button.addEventListener('click',() => {
  applyTheme(button.dataset.themeValue);
  try { localStorage.setItem('ieee-hub-theme',button.dataset.themeValue); } catch (_) { /* Choice still applies in this tab. */ }
  if (picker) { picker.open = false; picker.querySelector('summary').focus({preventScroll:true}); }
 }));
 window.addEventListener('storage',event => { if (event.key === 'ieee-hub-theme') applyTheme(event.newValue); });
 const toggle = document.querySelector('[data-nav-toggle]');
 const nav = document.getElementById('primary-nav');
 function closeNavigation() {
  if (!toggle || !nav) return;
  nav.removeAttribute('data-open');
  toggle.setAttribute('aria-expanded','false');
  toggle.setAttribute('aria-label','Abrir navegação');
 }
 if (toggle && nav) {
  toggle.hidden = false;
  toggle.addEventListener('click',() => {
   const open = toggle.getAttribute('aria-expanded') !== 'true';
   nav.toggleAttribute('data-open',open);
   toggle.setAttribute('aria-expanded',String(open));
   toggle.setAttribute('aria-label',open ? 'Fechar navegação' : 'Abrir navegação');
  });
  window.matchMedia('(min-width:60.01rem)').addEventListener('change',closeNavigation);
 }
 document.addEventListener('keydown',event => {
  if (event.key !== 'Escape') return;
  if (picker?.open) { picker.open = false; picker.querySelector('summary').focus({preventScroll:true}); }
  if (toggle?.getAttribute('aria-expanded') === 'true') { closeNavigation(); toggle.focus({preventScroll:true}); }
 });
 document.addEventListener('click',event => { if (picker?.open && !picker.contains(event.target)) picker.open = false; });
})();
