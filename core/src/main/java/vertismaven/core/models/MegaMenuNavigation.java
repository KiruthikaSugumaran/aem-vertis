
package vertismaven.core.models;

import com.day.cq.wcm.api.Page;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.json.JSONException;
import org.json.JSONObject;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Iterator;

@Model(adaptables = Resource.class)
public class MegaMenuNavigation {

    @SlingObject
    private ResourceResolver resourceResolver;

    @Inject
    private String rootPath;

    private JSONObject jsonObject;

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    @PostConstruct
    // PostConstructs are called after all the injection has occurred, but before the Model object is returned for use.
    private void init() {
        jsonObject = new JSONObject(); // Initialize the jsonObject before using it
        
        Resource resource = resourceResolver.getResource(rootPath);
        if (resource != null) {
            Page currentPage = resource.adaptTo(Page.class);
            if (currentPage != null) {
            	Iterator<Page> childPageRes = currentPage.listChildren();
                while (childPageRes.hasNext()) {
                    Page page = childPageRes.next();
                    ValueMap pageProperties = page.getProperties();
                    boolean hideProperty = pageProperties.containsKey("hideInNavigation") ? pageProperties.get("hideInNavigation", Boolean.class) : false;
                    if (!hideProperty) {
                        String pageTitle = page.getTitle();
                        String pageName = page.getName();
                        try {
							jsonObject.put(pageName, pageTitle);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    }
                }
            }
        }
    }
}
