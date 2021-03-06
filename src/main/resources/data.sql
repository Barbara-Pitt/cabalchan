--insert into flags
insert into flags (filename,filetype,flagname) 
values 
('/flags/4th_international.png','image/png','4th_international')
,('/flags/acceleration.png','image/png','Acceleration')
,('/flags/ak-47.png','image/png','AK-47')
,('/flags/albania.png','image/png','Albania')
,('/flags/allende.png','image/png','Allende')
,('/flags/anarcha-feminism.png','image/png','Anarcha-Feminism')
,('/flags/anarchism.png','image/png','Anarchism')
,('/flags/anarcho-capitalism.png','image/png','Anarcho-Capitalism')
,('/flags/anarcho-communism.png','image/png','Anarcho-Communism')
,('/flags/anarcho-nihilism.png','image/png','Anarcho-Nihilism')
,('/flags/anarcho-primitivism.png','image/png','Anarcho-Primitivism')
,('/flags/antifa.png','image/png','Antifa')
,('/flags/armchair.png','image/png','Armchair')
,('/flags/atheism.png','image/png','Atheism')
,('/flags/bolshevik.png','image/png','Bolshevik')
,('/flags/brocialism.png','image/png','Brocialism')
,('/flags/burkina_faso.png','image/png','Burkina Faso')
,('/flags/ca.png','image/png','Canadien')
,('/flags/carlism.png','image/png','Carlism')
,('/flags/chavismo.png','image/png','Chavismo')
,('/flags/che.png','image/png','Che')
,('/flags/china.png','image/png','China')
,('/flags/christian_anarchism.png','image/png','Christian Anarchism')
,('/flags/christian_communism.png','image/png','Christian Communism')
,('/flags/cockshott.png','image/png','Cockshott')
,('/flags/council_communism.png','image/png','Council Communism')
,('/flags/cuba.png','image/png','Cuba')
,('/flags/ddr.png','image/png','DDR')
,('/flags/democrap.png','image/png','Democrap')
,('/flags/democratic_socialism.png','image/png','Democratic Socialism')
,('/flags/directx.png','image/png','Direct X')
,('/flags/dprk.png','image/png','DPRK')
,('/flags/egalitarianism.png','image/png','Egalitarianism')
,('/flags/egoism.png','image/png','Egoism')
,('/flags/eristocracy.png','image/png','Έριστοκρατία')
,('/flags/eureka.png','image/png','Eureka')
,('/flags/eurocommunism.png','image/png','Eurocommunism')
,('/flags/farc.png','image/png','Las FARC')
,('/flags/fed.png','image/png','Fed')
,('/flags/flq.png','image/png','Front de libération du Québec')
,('/flags/freud.png','image/png','Freud')
,('/flags/gadsden.png','image/png','Gadsden')
,('/flags/gay_nazi.png','image/png','Gay Nazi')
,('/flags/gentoo.png','image/png','Gentoo')
,('/flags/gorro.png','image/png','Gorro')
,('/flags/groucho_marxism.png','image/png','Groucho Marxism')
,('/flags/hammer_&_sickle.png','image/png','Hammer & Sickle')
,('/flags/international_brigade.png','image/png','International Brigade')
,('/flags/ira.png','image/png','IRA')
,('/flags/islamic_communism.png','image/png','Islamic Communism')
,('/flags/iww.png','image/png','IWW')
,('/flags/juche.png','image/png','Juche')
,('/flags/kampuchea.png','image/png','Kampuchea')
,('/flags/left_communism.png','image/png','Left Communism')
,('/flags/lenin_cap.png','image/png','Lenin Cap')
,('/flags/luck_o_the_irish.png','image/png','Luck O The Irish')
,('/flags/luxemburg.png','image/png','Luxemburg')
,('/flags/marx.png','image/png','Marx')
,('/flags/mutualism.png','image/png','Mutualism')
,('/flags/naxalite.png','image/png','Naxalite')
,('/flags/nazbol.png','image/png','Nazbol')
,('/flags/nazi.png','image/png','Nazi')
,('/flags/ndfp.png','image/png','NDFP')
,('/flags/palestine.png','image/png','Palestine')
,('/flags/pan-africanism.png','image/png','Pan-Africanism')
,('/flags/cenzopapa.png','image/png','Papież')
,('/flags/phrygian_cap.png','image/png','Phrygian Cap')
,('/flags/pirate.png','image/png','Pirate')
,('/flags/porky.png','image/png','Porky')
,('/flags/posadas.png','image/png','Posadas')
,('/flags/punk.png','image/png','Punk')
,('/flags/raised_fist.png','image/png','Raised Fist')
,('/flags/read_a_fucking_book.png','image/png','Read a Fucking Book')
,('/flags/rethuglican.png','image/png','Rethuglican')
,('/flags/sabo-tabby.png','image/png','Sabo-Tabby')
,('/flags/sandinista.png','image/png','Sandinista')
,('/flags/sendero_luminoso.png','image/png','Sendero Luminoso')
,('/flags/slavoj.png','image/png','Slavoj')
,('/flags/snibeti_snab.png','image/png','Snibeti Snab')
,('/flags/socialism.png','image/png','Socialism')
,('/flags/socrates.png','image/png','Socrates')
,('/flags/soviet_union.png','image/png','Soviet Union')
,('/flags/spurdo.png','image/png','Spurdo')
,('/flags/ssnp.png','image/png','SSNP')
,('/flags/stalin.png','image/png','Stalin')
,('/flags/syndicalism.png','image/png','Syndicalism')
,('/flags/tankie.png','image/png','Tankie')
,('/flags/technocracy.png','image/png','Technocracy')
,('/flags/think.png','image/png','Think')
,('/flags/transhumanism.png','image/png','Transhumanism')
,('/flags/united_farm_workers.png','image/png','United Farm Workers')
,('/flags/viet_cong.png','image/png','Viet Cong')
,('/flags/wiphala.png','image/png','Wiphala')
,('/flags/ypg.png','image/png','YPG')
,('/flags/yugoslavia.png','image/png','Yugoslavia')
,('/flags/zapatista.png','image/png','Zapatista') ON CONFLICT DO NOTHING;

--insert into filters
insert into filters (filtername,cssclass) 
values 
('1977','_1977')
,('Aden','aden')
,('Amaro', 'amaro')
,('Brannan', 'brannan')
,('Brooklyn', 'brooklyn')
,('Clarendon', 'clarendon')
,('Gingham', 'gingham')
,('Hudson', 'hudson')
,('Inkwell', 'inkwell')
,('Kelvin', 'kelvin')
,('Lark', 'lark')
,('Lo-fi', 'lofi')
,('Mayfair', 'mayfair')
,('Moon', 'moon')
,('Nashville', 'nashville')
,('Perpetua', 'perpetua')
,('Reyes', 'reyes')
,('Rise', 'rise')
,('Slumber', 'slumber')
,('Stinson', 'stinson')
,('Toaster', 'toaster')
,('Valencia', 'valencia')
,('Walden', 'walden')
,('Willow', 'willow')
,('X-Pro-2', 'xpro2') ON CONFLICT DO NOTHING;

--insert into categories
insert into categories (title,subtitle) 
values 
('current','Current Events, Politics, News')
,('eceleberry','Twitter, Social Media, Ecelebs and E-drama')
,('g', 'Computer Programming') 
,('lit', 'Books and Book reviews') 
,('mu', 'Music') 
,('tv', 'Television and Movies') 
,('v', 'Video Games')
ON CONFLICT DO NOTHING;
