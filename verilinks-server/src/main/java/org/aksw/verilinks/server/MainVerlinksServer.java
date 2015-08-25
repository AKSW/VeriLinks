package org.aksw.verilinks.server;

import java.io.File;
import java.net.URL;
import java.security.ProtectionDomain;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 *
 *
 * http://stackoverflow.com/questions/10738816/deploying-a-servlet-
 * programmatically-with-jetty
 * http://stackoverflow.com/questions/3718221/add-resources
 * -to-jetty-programmatically
 *
 * @author raven
 *
 *         http://kielczewski.eu/2013/11/using-embedded-jetty-spring-mvc/
 */
public class MainVerlinksServer {

    public static void main(String[] args) {
        startServer(8080);
    }
    
    public static Server startServer(int port) {
        // Not sure if using this class always works as expected
        Server result = startServer(MainVerlinksServer.class, port);
        return result;
    }

    public static Server startServer(Class<?> clazz, int port) {

        ProtectionDomain protectionDomain = clazz.getProtectionDomain();
        URL location = protectionDomain.getCodeSource().getLocation();
        String externalForm = location.toExternalForm();

        System.out.println("External form: " + externalForm);

        // Try to detect whether we are being run from an
        // archive (uber jar / war) or just from compiled classes
        if (externalForm.endsWith("/classes/")) {

            String test = "src/main/webapp";
            File file = new File(test);
            if(file.exists()) {
                externalForm = test;
            }
        }

        System.out.println("Loading webAppContext from " + externalForm);

        Server result = startServer(port, externalForm);
        return result;
    }

    public static Server startServer(int port, String externalForm) {
        Server server = new Server(port);
        // server.setHandler(getServletContextHandler(getContext()));

        // SocketConnector connector = new SocketConnector();
        //
        // // Set some timeout options to make debugging easier.
        // connector.setMaxIdleTime(1000 * 60 * 60);
        // connector.setSoLingerTime(-1);
        // connector.setPort(port);
        // server.setConnectors(new Connector[] { connector });

        final WebAppContext webAppContext = new WebAppContext();

        // AnnotationConfigWebApplicationContext rootContext = new
        // AnnotationConfigWebApplicationContext();
        // rootContext.register(AppConfig.class);
        //
        // // Manage the lifecycle of the root application context
        // webAppContext.addEventListener(new
        // ContextLoaderListener(rootContext));
        // webAppContext.addEventListener(new RequestContextListener());

        // webAppContext.addEventListener(new ContextLoaderListener(context);
        //Context servletContext = webAppContext.getServletContext();

//        webAppContext.addLifeCycleListener(new AbstractLifeCycleListener() {
//            @Override
//            public void lifeCycleStarting(LifeCycle arg0) {
//                // WebAppInitializer initializer = new WebAppInitializer();
//                try {
//                    Context servletContext = webAppContext.getServletContext();
//                    // servletContext.setExtendedListenerTypes(true);
//                    initializer.onStartup(servletContext);
//                } catch (ServletException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        });

        webAppContext.setServer(server);
        webAppContext.setContextPath("/");

        //servletContextsetDescriptor();
        webAppContext.setDescriptor(externalForm + "/WEB-INF/web.xml");
        webAppContext.setWar(externalForm);

        server.setHandler(webAppContext);
        try {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return server;
    }

    // public void mainGrizzly() {
    // HttpServer server = new HttpServer();
    //
    // final NetworkListener listener = new NetworkListener("grizzly",
    // NetworkListener.DEFAULT_NETWORK_HOST, PACS.RESTPort);
    // server.addListener(listener);
    //
    // ResourceConfig rc = new ResourceConfig();
    // rc.packages("org.aksw.facete2.web");
    // HttpHandler processor =
    // ContainerFactory.createContainer(GrizzlyHttpContainer.class, rc);
    // server.getServerConfiguration().addHttpHandler(processor, "");
    // }
}
