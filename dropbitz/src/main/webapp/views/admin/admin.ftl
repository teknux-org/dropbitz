<#import "/views/layout/layout.ftl" as layout>

<@layout.layout>
    <div class="container">
        <div class="row">
            <div class="col-md-3">
                <h3>Menu</h3>
                <hr/>
                <ul class="nav nav-stacked">
                    <li><a href=""><i class="glyphicon glyphicon-user"></i> Profile</a></li>
                    <li><a href=""><i class="glyphicon glyphicon-link"></i> Secure Id</a></li>
                    <li><a href=""><i class="glyphicon glyphicon-send"></i> Alerts</a></li>
                    <li><a href=""><i class="glyphicon glyphicon-lock"></i> Users</a></li>
                </ul>
            </div>
            <div class="col-md-9">
                <h3>Page</h3>
                <hr/>
            </div>
        </div>
    </div>
</@layout.layout>