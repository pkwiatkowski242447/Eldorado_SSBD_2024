import {Card, CardContent, CardDescription, CardHeader, CardTitle} from "@/components/ui/card.tsx";
import eldoLogo from "@/assets/eldorado.png";
import {Button} from "@/components/ui/button.tsx";
import {z} from "zod";
import {useToast} from "@/components/ui/use-toast.ts";
import {useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {api} from "@/api/api.ts";
import {Form, FormControl, FormField, FormItem, FormLabel, FormMessage} from "@/components/ui/form.tsx";
import {Input} from "@/components/ui/input.tsx";
import {useTranslation} from "react-i18next";
import handleApiError from "@/components/HandleApiError.ts";
import {useState} from "react";
import {Loader2} from "lucide-react";

function RestoreAccessPage() {
    const {toast} = useToast()
    const {t} = useTranslation();
    const [isLoading, setIsLoading] = useState(false);

    const formSchema = z.object({
        email: z.string().min(1, {message: t("forgotPasswordPage.emptyEmail")})
            .email(t("forgotPasswordPage.wrongEmail")),
    })

    const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            email: "",
        },
    })

    function onSubmit(values: z.infer<typeof formSchema>) {
        setIsLoading(true)
        api.restoreAccess(values.email)
            .then(() => {
                toast({
                    title: t("restoreAccessPage.popUp.restoreAccessOK.title"),
                    description: t("restoreAccessPage.popUp.restoreAccessOK.text"),
                });
            })
            .catch((error) => {
                handleApiError(error);
            }).finally(() => {
            setIsLoading(false)
        });
    }

    return (
        <div className="flex flex-col items-center justify-center">
            <a href="/home" className="flex items-center">
                <img src={eldoLogo} alt="Eldorado" className="mx-auto h-auto w-1/2"/>
                <span className="sr-only">Eldorado</span>
            </a>
            <Card className="mx-auto max-w-2xl">
                <CardHeader>
                    <CardTitle>{t("restoreAccessPage.title")}</CardTitle>
                    <CardDescription>{t("restoreAccessPage.info")}</CardDescription>
                </CardHeader>
                <CardContent>
                    <Form {...form}>
                        <form onSubmit={form.handleSubmit(onSubmit)}>
                            <div className="flex flex-col space-y-4">
                                <div>
                                    <FormField
                                        control={form.control}
                                        name="email"
                                        render={({field}) => (
                                            <FormItem>
                                                <FormLabel
                                                    className="text-black">{t("restoreAccessPage.email")}</FormLabel>
                                                <FormControl>
                                                    <Input placeholder="mail@example.com" {...field}/>
                                                </FormControl>
                                                <FormMessage/>
                                            </FormItem>
                                        )}
                                    />
                                </div>
                                <Button type="submit" className="w-full" disabled={isLoading}>
                                    {isLoading ? (
                                        <>
                                            <Loader2 className="mr-2 h-4 w-4 animate-spin"/>
                                        </>
                                    ) : (
                                        t("restoreAccessPage.submit")
                                    )}
                                </Button>
                            </div>
                        </form>
                    </Form>
                </CardContent>
            </Card>
        </div>
    );
}

export default RestoreAccessPage;