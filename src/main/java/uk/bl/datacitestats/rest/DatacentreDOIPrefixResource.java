package uk.bl.datacitestats.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.restlet.ext.guice.SelfInjectingServerResource;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;

import uk.bl.datacitestats.misc.DOIPrefixMapper;

import com.google.common.base.Joiner;
import com.google.common.collect.Multimap;

public class DatacentreDOIPrefixResource extends SelfInjectingServerResource {

	@Inject
	DOIPrefixMapper mapper;

	@Get("json")
	public Representation getPrefixMap() {
		return new JacksonRepresentation<List<TypeaheadJSBean>>(TypeaheadJSBean.fromMultimap(mapper.getDatacentreMap()));
	}

	public static class TypeaheadJSBean {
		private static Joiner joiner = Joiner.on('|');

		public TypeaheadJSBean(String publisher, String doi) {
			this.value = publisher;
			this.doi = doi;
		}

		public TypeaheadJSBean(String publishers, Collection<String> dois) {
			this.value = publishers;
			this.doi = joiner.join(dois);
		}

		public String value;
		public String doi;

		public static List<TypeaheadJSBean> fromMultimap(Multimap<String, String> m) {
			List<TypeaheadJSBean> list = new ArrayList<TypeaheadJSBean>();
			for (String s : m.keySet())
				list.add(new TypeaheadJSBean(s, m.get(s)));
			return list;
		}
	}

}
