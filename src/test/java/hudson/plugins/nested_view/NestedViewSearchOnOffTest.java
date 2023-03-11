/*
 * The MIT License
 *
 * Copyright (c) 2004-2011, Sun Microsystems, Inc., Alan Harder
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package hudson.plugins.nested_view;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.Issue;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.JenkinsRule.WebClient;

import static org.junit.Assert.assertNotNull;

public class NestedViewSearchOnOffTest {

    @Rule
    public JenkinsRule rule = new JenkinsRule();


    @Test
    @Issue("JENKINS-65924?")
    public void testSearchWithPrefixOnOf() throws Exception {
        WebClient wc = NestedViewTest.createViewAndJobsForNEstedViewSearch(rule);
        // Perform some searches with extended search on. Results should contain urls with prefix
        NestedViewGlobalConfig.getInstance().setNestedViewSearch(true);
        assertNotNull(NestedViewTest.searchAndCheck1(wc, rule));
        assertNotNull(NestedViewTest.searchAndCheck2(wc, rule));
        assertNotNull(NestedViewTest.searchAndCheck3(wc, rule));
        assertNotNull(NestedViewTest.searchAndCheck4(wc, rule));
        HtmlPage page = wc.search("-r: .*nest.*");
        HtmlAnchor html = page.getAnchorByHref(rule.getURL().toString() + "view/test-nest");
        assertNotNull(html);
        //second still ok
        page = wc.search("-rX: .*nest.*");
        html = page.getAnchorByHref(rule.getURL().toString() + "view/test-nest");
        assertNotNull(html);
        //X disabeld our search for next search
        Exception ex = null;
        html = null;
        page = null;
        try {
            page = wc.search("-rX: .*nest.*");
            html = page.getAnchorByHref(rule.getURL().toString() + "view/test-nest");
        } catch (FailingHttpStatusCodeException exx) {
            ex = exx;
        }
        assertNotNull(ex);
    }
}