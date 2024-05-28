import {zodResolver} from "@hookform/resolvers/zod";
import {useForm} from "react-hook-form";
import {z} from "zod";
import {Button} from "@/components/ui/button";
import {Form, FormControl, FormField, FormItem, FormLabel, FormMessage} from "@/components/ui/form";
import {RadioGroup, RadioGroupItem} from "@/components/ui/radio-group";
import {Card} from "@/components/ui/card.tsx";
import {useAccountState} from "@/context/AccountContext.tsx";
import {useNavigate} from "react-router-dom";
import {useEffect} from "react";
import {useAccount} from "@/hooks/useAccount.ts";
import {useTranslation} from "react-i18next";
import {
    Breadcrumb,
    BreadcrumbItem,
    BreadcrumbLink,
    BreadcrumbList,
    BreadcrumbSeparator
} from "@/components/ui/breadcrumb.tsx";
import {Slash} from "lucide-react";
import {Pathnames} from "@/router/pathnames.ts";
import {Badge} from "@/components/ui/badge.tsx";

const FormSchema = z.object({
    type: z.any()
});

const roleNames = {
    ADMIN: "Admin",
    STAFF: "Staff",
    CLIENT: "Client",
};

const roleOrder = ["ADMIN", "STAFF", "CLIENT"];

function ChangeUserLevelPage() {
    const navigate = useNavigate();
    const {account} = useAccountState();
    const {getCurrentAccount} = useAccount();
    const {t} = useTranslation();

    const form = useForm<z.infer<typeof FormSchema>>({
        resolver: zodResolver(FormSchema),
    });

    useEffect(() => {
        getCurrentAccount();
    }, []);

    const orderedUserLevels = account?.userLevelsDto
        ?.slice()
        .sort((a, b) => roleOrder.indexOf(a.roleName) - roleOrder.indexOf(b.roleName)) || [];

    function onSubmit(data: z.infer<typeof FormSchema>) {
        try {
            if (account) {
                const newActiveUserLevel
                    = account.userLevelsDto.find((userLevel) => userLevel.roleName === data.type);
                if (newActiveUserLevel) {
                    account.activeUserLevel = newActiveUserLevel;
                    localStorage.setItem('account', JSON.stringify(account));
                    localStorage.setItem('chosenUserLevel', newActiveUserLevel.roleName);
                }
            }
            navigate(Pathnames.loggedIn.home);
        } catch (e) {
            console.log(e);
        }
    }

    return (
        <div className="flex min-h-screen w-full flex-col">
            <div className="flex justify-between items-center pt-2">
                <Breadcrumb className={"pl-2"}>
                    <BreadcrumbList>
                        <BreadcrumbItem>
                            <BreadcrumbLink href="/home">{t("breadcrumb.home")}</BreadcrumbLink>
                        </BreadcrumbItem>
                        <BreadcrumbSeparator>
                            <Slash/>
                        </BreadcrumbSeparator>
                        <BreadcrumbItem>
                            <BreadcrumbLink>{t("breadcrumb.changeUserlevel")}</BreadcrumbLink>
                        </BreadcrumbItem>
                    </BreadcrumbList>
                </Breadcrumb>
                <Button variant={"ghost"} disabled={true}/>
            </div>
            <main
                className="flex min-h-[calc(100vh_-_theme(spacing.16))] flex-1 flex-col gap-4 bg-muted/40 p-4 md:gap-8 md:p-10">
                <div className="mx-auto grid w-full max-w-6xl gap-2">
                    <h1 className="text-3xl font-semibold">{t("siteHeader.changeLevel.select")}</h1>
                </div>
                <div className="flex justify-center items-center mx-auto p-10 w-auto">
                    <Card className="mx-10 w-auto">
                        <Form {...form}>
                            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-5 p-5">
                                <FormField
                                    control={form.control}
                                    name="type"
                                    render={({field}) => (
                                        <FormItem className="space-y-3">
                                            <FormControl>
                                                <RadioGroup
                                                    onValueChange={field.onChange}
                                                    defaultValue={account?.activeUserLevel?.roleName}
                                                    className="flex flex-col space-y-1"
                                                >
                                                    {orderedUserLevels.map((userLevel, index) => (
                                                        <FormItem key={index}
                                                                  className="flex items-center space-x-3 space-y-0">
                                                            <FormControl>
                                                                <RadioGroupItem value={userLevel.roleName}/>
                                                            </FormControl>
                                                            <FormLabel>
                                                                <Badge
                                                                    variant={"secondary"}>{roleNames[userLevel.roleName]}</Badge>
                                                            </FormLabel>
                                                        </FormItem>
                                                    ))}
                                                </RadioGroup>
                                            </FormControl>
                                            <FormMessage/>
                                        </FormItem>
                                    )}
                                />
                                <Button type="submit">{t("siteHeader.changeLevel.select.save")}</Button>
                            </form>
                        </Form>
                    </Card>
                </div>
            </main>
        </div>
    );
}

export default ChangeUserLevelPage;
